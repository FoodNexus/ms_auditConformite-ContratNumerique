package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.ms_auditConformite_ContratNumerique.service.AuditStatisticsService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditStatisticsControllerTest {

    @Mock
    private AuditStatisticsService service;

    @InjectMocks
    private AuditStatisticsController controller;

    @Test
    void getGlobalStats_ShouldReturnMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("totalInspections", 10L);
        when(service.getGlobalStats(100L)).thenReturn(map);

        Map<String, Object> result = controller.getGlobalStats(100L);
        assertEquals(10L, result.get("totalInspections"));
    }
}
