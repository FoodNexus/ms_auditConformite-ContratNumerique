package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import java.util.List;

public interface RecyclingProductsRepository
        extends JpaRepository<RecyclingProducts, Long> {

    List<RecyclingProducts> findByInspectionCase_CaseId(Long caseId);
    List<RecyclingProducts> findByDestination(
            RecyclingProducts.Destination destination);
}