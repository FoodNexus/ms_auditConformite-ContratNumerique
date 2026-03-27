package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;

import java.util.List;

public interface RecyclingLogRepository
        extends JpaRepository<RecyclingProducts, Long> {

    List<RecyclingProducts> findByInspectionCase_CaseId(Long caseId);
}