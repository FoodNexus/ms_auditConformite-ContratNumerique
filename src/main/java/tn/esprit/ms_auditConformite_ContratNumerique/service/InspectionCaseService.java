package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionCaseService {

    private final InspectionCaseRepository repo;
    private final RecyclingProductsRepository recyclingProductsRepository;

    // CREATE
    public InspectionCase create(InspectionCase inspectionCase) {
        inspectionCase.setCreationDate(LocalDate.now());
        inspectionCase.setResolutionStatus(
                InspectionCase.ResolutionStatus.EN_COURS
        );

        // Sauvegarder l'InspectionCase
        InspectionCase saved = repo.save(inspectionCase);

        // Si verdict = DESTRUCTION_RECYCLAGE → créer automatiquement RecyclingProducts
        if (saved.getSanitaryVerdict() ==
                InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE) {

            RecyclingProducts recyclingProduct = RecyclingProducts.builder()
                    .transferDate(LocalDate.now())
                    .inspectionCase(saved)
                    .weight(null)        // ← sera rempli après
                    .destination(null)   // ← sera rempli après
                    .build();

            recyclingProductsRepository.save(recyclingProduct);
        }

        return saved;
    }

    // READ ALL
    public List<InspectionCase> getAll() {
        return repo.findAll();
    }

    // READ BY ID
    public InspectionCase getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("InspectionCase non trouvé: " + id));
    }

    // READ BY AUDITOR
    public List<InspectionCase> getByAuditor(Long auditorId) {
        return repo.findByAuditorId(auditorId);
    }

    // READ BY DELIVERY
    public List<InspectionCase> getByDelivery(Long deliveryId) {
        return repo.findByDeliveryId(deliveryId);
    }

    // READ BY STATUS
    public List<InspectionCase> getByStatus(
            InspectionCase.ResolutionStatus status) {
        return repo.findByResolutionStatus(status);
    }

    // READ BY VERDICT
    public List<InspectionCase> getByVerdict(
            InspectionCase.SanitaryVerdict verdict) {
        return repo.findBySanitaryVerdict(verdict);
    }

    // UPDATE
    public InspectionCase update(Long id, InspectionCase updated) {
        InspectionCase existing = getById(id);
        existing.setDescription(updated.getDescription());
        existing.setResolutionStatus(updated.getResolutionStatus());
        existing.setSanitaryVerdict(updated.getSanitaryVerdict());
        existing.setAuditorId(updated.getAuditorId());
        existing.setDeliveryId(updated.getDeliveryId());
        return repo.save(existing);
    }

    // UPDATE STATUS
    public InspectionCase updateStatus(Long id,
                                       InspectionCase.ResolutionStatus status) {
        InspectionCase existing = getById(id);
        existing.setResolutionStatus(status);
        return repo.save(existing);
    }

    // UPDATE VERDICT
    public InspectionCase updateVerdict(Long id,
                                        InspectionCase.SanitaryVerdict verdict) {
        InspectionCase existing = getById(id);
        existing.setSanitaryVerdict(verdict);

        // Si on change le verdict vers DESTRUCTION_RECYCLAGE
        // et qu'il n'y a pas encore de RecyclingProducts → créer automatiquement
        if (verdict == InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE) {
            boolean alreadyExists = !recyclingProductsRepository
                    .findByInspectionCase_CaseId(id).isEmpty();

            if (!alreadyExists) {
                RecyclingProducts recyclingProduct = RecyclingProducts.builder()
                        .transferDate(LocalDate.now())
                        .inspectionCase(existing)
                        .weight(null)
                        .destination(null)
                        .build();
                recyclingProductsRepository.save(recyclingProduct);
            }
        }

        return repo.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("InspectionCase non trouvé: " + id);
        }
        repo.deleteById(id);
    }
}