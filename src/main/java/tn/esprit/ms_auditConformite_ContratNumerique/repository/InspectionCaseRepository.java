package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import java.util.List;

public interface InspectionCaseRepository
        extends JpaRepository<InspectionCase, Long> {

    List<InspectionCase> findByAuditorId(Long auditorId);
    List<InspectionCase> findByDelevry_to(String delevry_to);
    List<InspectionCase> findByResolutionStatus(
            InspectionCase.ResolutionStatus status);
    List<InspectionCase> findBySanitaryVerdict(
            InspectionCase.SanitaryVerdict verdict);
}