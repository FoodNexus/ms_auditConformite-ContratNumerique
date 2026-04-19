package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditStatisticsService {

    private final InspectionCaseRepository inspectionRepo;
    private final RecyclingProductsRepository recyclingRepo;

    public Map<String, Object> getGlobalStats(Long auditorId) {
        Map<String, Object> stats = new HashMap<>();

        // Inspection stats
        stats.put("verdictDistribution", inspectionRepo.countByVerdict(auditorId));
        stats.put("statusBreakdown", inspectionRepo.countByStatus(auditorId));
        stats.put("monthlyInspections", inspectionRepo.countByMonth(auditorId));
        stats.put("conversionRate", inspectionRepo.getConversionRate(auditorId));

        // Recycling stats
        stats.put("weightByDestination", recyclingRepo.sumWeightByDestination(auditorId));
        stats.put("monthlyRecyclingVolume", recyclingRepo.sumWeightByMonth(auditorId));
        stats.put("weightByVerdict", recyclingRepo.sumWeightByVerdict(auditorId));
        stats.put("countByDestination", recyclingRepo.countByDestination(auditorId));
        stats.put("averageWeight", recyclingRepo.getAverageWeight(auditorId));

        // Global counts
        long totalInspections = inspectionRepo.count();
        long auditorInspections = auditorId == null ? totalInspections : inspectionRepo.findByAuditorId(auditorId).size();
        stats.put("totalInspections", auditorInspections);
        stats.put("totalGlobalInspections", totalInspections);
        stats.put("totalRecyclingItems", auditorId == null ? recyclingRepo.count() : recyclingRepo.countByDestination(auditorId).size());

        // --- NEW AUDITOR RELATIVE STATS ---
        if (auditorId != null) {
            stats.put("auditorTopDestinations", inspectionRepo.countTopDestinations(auditorId));
            stats.put("auditorMonthlyResolved", inspectionRepo.countMonthlyResolved(auditorId));
            stats.put("auditorMonthlyConformity", inspectionRepo.countMonthlyConformity(auditorId));
            
            Double auditorWeight = recyclingRepo.sumTotalWeight(auditorId);
            Double globalWeight = recyclingRepo.sumGlobalTotalWeight();
            stats.put("auditorWeightContribution", (auditorWeight != null && globalWeight != null && globalWeight > 0) ? (auditorWeight * 100.0 / globalWeight) : 0);
            stats.put("auditorTotalWeight", auditorWeight != null ? auditorWeight : 0);
        }

        return stats;
    }
}
