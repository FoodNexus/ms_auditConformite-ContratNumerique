package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import java.util.List;
import java.util.Map;

public interface InspectionCaseRepository
        extends JpaRepository<InspectionCase, Long> {

    List<InspectionCase> findByAuditorId(Long auditorId);
    List<InspectionCase> findByDelevryTo(String delevryTo);
    List<InspectionCase> findByResolutionStatus(
            InspectionCase.ResolutionStatus status);
    List<InspectionCase> findBySanitaryVerdict(
            InspectionCase.SanitaryVerdict verdict);

    @Query("SELECT i.sanitaryVerdict as label, COUNT(i) as value FROM InspectionCase i " +
            "WHERE (:auditorId IS NULL OR i.auditorId = :auditorId) GROUP BY i.sanitaryVerdict")
    List<Map<String, Object>> countByVerdict(@Param("auditorId") Long auditorId);

    @Query("SELECT i.resolutionStatus as label, COUNT(i) as value FROM InspectionCase i " +
            "WHERE (:auditorId IS NULL OR i.auditorId = :auditorId) GROUP BY i.resolutionStatus")
    List<Map<String, Object>> countByStatus(@Param("auditorId") Long auditorId);

    @Query("SELECT FUNCTION('MONTH', i.creationDate) as month, COUNT(i) as value FROM InspectionCase i" +
            " WHERE (:auditorId IS NULL OR i.auditorId = :auditorId) GROUP BY FUNCTION('MONTH', i.creationDate) ORDER BY 1")
    List<Map<String, Object>> countByMonth(@Param("auditorId") Long auditorId);

    @Query("SELECT (SELECT COUNT(r) FROM RecyclingProducts r" +
            " WHERE (:auditorId IS NULL OR r.inspectionCase.auditorId = :auditorId)) * 100.0 / (SELECT COUNT(i) FROM InspectionCase i " +
            "WHERE (:auditorId IS NULL OR i.auditorId = :auditorId))")
    Double getConversionRate(@Param("auditorId") Long auditorId);

    @Query("SELECT i.delevryTo as label, COUNT(i) as value FROM InspectionCase i" +
            " WHERE (:auditorId IS NULL OR i.auditorId = :auditorId) GROUP BY i.delevryTo ORDER BY COUNT(i) DESC")
    List<Map<String, Object>> countTopDestinations(@Param("auditorId") Long auditorId);

    @Query("SELECT FUNCTION('MONTH', i.creationDate) as month, COUNT(i) as value FROM InspectionCase i" +
            " WHERE (:auditorId IS NULL OR i.auditorId = :auditorId) AND i.resolutionStatus = 'RESOLU' GROUP BY FUNCTION('MONTH', i.creationDate) ORDER BY 1")
    List<Map<String, Object>> countMonthlyResolved(@Param("auditorId") Long auditorId);

    @Query("SELECT FUNCTION('MONTH', i.creationDate) as month, COUNT(i) as value FROM InspectionCase i" +
            " WHERE (:auditorId IS NULL OR i.auditorId = :auditorId) AND i.sanitaryVerdict = 'PROPRE_A_LA_CONSOMMATION' GROUP BY FUNCTION('MONTH', i.creationDate) ORDER BY 1")
    List<Map<String, Object>> countMonthlyConformity(@Param("auditorId") Long auditorId);
}