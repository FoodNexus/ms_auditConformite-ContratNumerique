package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditScheduler {

    private final InspectionCaseRepository inspectionRepo;
    private final RecyclingProductsRepository recyclingRepo;
    private final AppNotificationService notificationService;

    // Run every minute
    @Scheduled(fixedRate = 60000)
    public void checkAuditTasks() {
        checkLateInspections();
        checkIncompleteRecycling();
    }

    // Every 2 minutes
    @Scheduled(cron = "0 */2 * * * *")
    public void flushNotifications() {
        notificationService.deleteAllNotifs();
        System.out.println("--- Notifications Flushed ---");
    }

    private void checkLateInspections() {
        LocalDateTime limit = LocalDateTime.now().minusMinutes(5);
        List<InspectionCase> lateCases = inspectionRepo.findAll().stream()
                .filter(c -> c.getResolutionStatus() == InspectionCase.ResolutionStatus.EN_COURS)
                .filter(c -> c.getCreationDate() != null && c.getCreationDate().isBefore(limit))
                .toList();

        for (InspectionCase c : lateCases) {
            String msg = "Alerte: L'inspection #" + c.getCaseId() + " est en cours depuis plus de 5 minutes.";
            notificationService.createNotification(msg, "ALERTE", c.getAuditorId());
        }
    }

    private void checkIncompleteRecycling() {
        List<RecyclingProducts> incomplete = recyclingRepo.findAll().stream()
                .filter(r -> r.getWeight() == null || r.getDestination() == null)
                .toList();

        for (RecyclingProducts r : incomplete) {
            Long auditorId = (r.getInspectionCase() != null) ? r.getInspectionCase().getAuditorId() : null;
            String msg = "Rappel: Le produit recyclé pour l'inspection #"
                    + (r.getInspectionCase() != null ? r.getInspectionCase().getCaseId() : "?")
                    + " nécessite un poids et une destination.";
            notificationService.createNotification(msg, "RAPPEL", auditorId);
        }
    }
}
