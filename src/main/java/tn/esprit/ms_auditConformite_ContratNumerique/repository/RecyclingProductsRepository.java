package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import java.util.List;
import java.util.Map;

public interface RecyclingProductsRepository
        extends JpaRepository<RecyclingProducts, Long> {

    List<RecyclingProducts> findByInspectionCase_CaseId(Long caseId);
    List<RecyclingProducts> findByDestination(
            RecyclingProducts.Destination destination);

    @Query("SELECT r.destination as label, SUM(r.weight) as value FROM RecyclingProducts r WHERE (:auditorId IS NULL OR r.inspectionCase.auditorId = :auditorId) GROUP BY r.destination")
    List<Map<String, Object>> sumWeightByDestination(@Param("auditorId") Long auditorId);

    @Query("SELECT FUNCTION('MONTH', r.transferDate) as month, SUM(r.weight) as value FROM RecyclingProducts r WHERE (:auditorId IS NULL OR r.inspectionCase.auditorId = :auditorId) GROUP BY FUNCTION('MONTH', r.transferDate) ORDER BY 1")
    List<Map<String, Object>> sumWeightByMonth(@Param("auditorId") Long auditorId);

    @Query("SELECT r.inspectionCase.sanitaryVerdict as label, SUM(r.weight) as value FROM RecyclingProducts r WHERE (:auditorId IS NULL OR r.inspectionCase.auditorId = :auditorId) GROUP BY r.inspectionCase.sanitaryVerdict")
    List<Map<String, Object>> sumWeightByVerdict(@Param("auditorId") Long auditorId);

    @Query("SELECT r.destination as label, COUNT(r) as value FROM RecyclingProducts r WHERE (:auditorId IS NULL OR r.inspectionCase.auditorId = :auditorId) GROUP BY r.destination")
    List<Map<String, Object>> countByDestination(@Param("auditorId") Long auditorId);

    @Query("SELECT AVG(r.weight) FROM RecyclingProducts r WHERE (:auditorId IS NULL OR r.inspectionCase.auditorId = :auditorId)")
    Double getAverageWeight(@Param("auditorId") Long auditorId);
}