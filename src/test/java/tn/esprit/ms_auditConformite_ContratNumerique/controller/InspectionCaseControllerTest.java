package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.service.InspectionCaseService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InspectionCaseControllerTest {

    @Mock
    private InspectionCaseService service;

    @InjectMocks
    private InspectionCaseController controller;

    private InspectionCase inspectionCase;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        inspectionCase = InspectionCase.builder().caseId(1L).build();
        mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());
    }

    @Test
    void create_ShouldReturnCreated() {
        when(service.create(inspectionCase)).thenReturn(inspectionCase);
        ResponseEntity<InspectionCase> response = controller.create(inspectionCase);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inspectionCase, response.getBody());
    }

    @Test
    void scan_ShouldReturnCreated() {
        when(service.scanAndCreate(mockFile, 1L, "DEL", "Desc")).thenReturn(inspectionCase);
        ResponseEntity<InspectionCase> response = controller.scan(mockFile, 1L, "DEL", "Desc");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inspectionCase, response.getBody());
    }

    @Test
    void analyze_ShouldReturnMap() {
        Map<String, Object> map = new HashMap<>();
        when(service.analyseImage(mockFile)).thenReturn(map);
        ResponseEntity<Map<String, Object>> response = controller.analyze(mockFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(map, response.getBody());
    }

    @Test
    void analyse_ShouldReturnMap() {
        Map<String, Object> map = new HashMap<>();
        when(service.analyseImage(mockFile)).thenReturn(map);
        ResponseEntity<Map<String, Object>> response = controller.analyse(mockFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(map, response.getBody());
    }

    @Test
    void getAll_ShouldReturnList() {
        when(service.getAll()).thenReturn(Arrays.asList(inspectionCase));
        List<InspectionCase> list = controller.getAll();
        assertEquals(1, list.size());
    }

    @Test
    void getById_ShouldReturnOk() {
        when(service.getById(1L)).thenReturn(inspectionCase);
        ResponseEntity<InspectionCase> response = controller.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inspectionCase, response.getBody());
    }

    @Test
    void getByAuditor_ShouldReturnList() {
        when(service.getByAuditor(1L)).thenReturn(Arrays.asList(inspectionCase));
        List<InspectionCase> list = controller.getByAuditor(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getByDelivery_ShouldReturnList() {
        when(service.getByDelivery("DEL")).thenReturn(Arrays.asList(inspectionCase));
        List<InspectionCase> list = controller.getByDelivery("DEL");
        assertEquals(1, list.size());
    }

    @Test
    void getByStatus_ShouldReturnList() {
        when(service.getByStatus(InspectionCase.ResolutionStatus.EN_COURS)).thenReturn(Arrays.asList(inspectionCase));
        List<InspectionCase> list = controller.getByStatus(InspectionCase.ResolutionStatus.EN_COURS);
        assertEquals(1, list.size());
    }

    @Test
    void getByVerdict_ShouldReturnList() {
        when(service.getByVerdict(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION)).thenReturn(Arrays.asList(inspectionCase));
        List<InspectionCase> list = controller.getByVerdict(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION);
        assertEquals(1, list.size());
    }

    @Test
    void update_ShouldReturnOk() {
        when(service.update(1L, inspectionCase)).thenReturn(inspectionCase);
        ResponseEntity<InspectionCase> response = controller.update(1L, inspectionCase);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inspectionCase, response.getBody());
    }

    @Test
    void updateStatus_ShouldReturnOk() {
        when(service.updateStatus(1L, InspectionCase.ResolutionStatus.RESOLU)).thenReturn(inspectionCase);
        ResponseEntity<InspectionCase> response = controller.updateStatus(1L, InspectionCase.ResolutionStatus.RESOLU);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inspectionCase, response.getBody());
    }

    @Test
    void updateVerdict_ShouldReturnOk() {
        when(service.updateVerdict(1L, InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION)).thenReturn(inspectionCase);
        ResponseEntity<InspectionCase> response = controller.updateVerdict(1L, InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inspectionCase, response.getBody());
    }

    @Test
    void delete_ShouldReturnNoContent() {
        ResponseEntity<Void> response = controller.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete(1L);
    }
}
