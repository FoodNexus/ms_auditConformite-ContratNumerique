package tn.esprit.ms_auditConformite_ContratNumerique.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditStatisticsServiceTest {

    @Mock
    private InspectionCaseRepository inspectionRepo;

    @Mock
    private RecyclingProductsRepository recyclingRepo;

    @InjectMocks
    private AuditStatisticsService service;

    @Test
    void getGlobalStats_WithNullAuditor_ShouldReturnGlobalStats() {
        when(inspectionRepo.count()).thenReturn(10L);
        when(recyclingRepo.count()).thenReturn(5L);

        Map<String, Object> stats = service.getGlobalStats(null);

        assertNotNull(stats);
        assertEquals(10L, stats.get("totalInspections"));
        assertEquals(10L, stats.get("totalGlobalInspections"));
        assertEquals(5L, stats.get("totalRecyclingItems"));
    }

    @Test
    void getGlobalStats_WithAuditorId_ShouldReturnAuditorStats() {
        Long auditorId = 100L;
        when(inspectionRepo.count()).thenReturn(100L);
        when(inspectionRepo.findByAuditorId(auditorId)).thenReturn(new ArrayList<>());
        when(recyclingRepo.countByDestination(auditorId)).thenReturn(new ArrayList<>());
        
        when(recyclingRepo.sumTotalWeight(auditorId)).thenReturn(50.0);
        when(recyclingRepo.sumGlobalTotalWeight()).thenReturn(200.0);

        Map<String, Object> stats = service.getGlobalStats(auditorId);

        assertNotNull(stats);
        assertEquals(0L, stats.get("totalInspections"));
        assertEquals(100L, stats.get("totalGlobalInspections"));
        assertEquals(0L, stats.get("totalRecyclingItems"));
        assertEquals(25.0, stats.get("auditorWeightContribution"));
        assertEquals(50.0, stats.get("auditorTotalWeight"));
    }
}
