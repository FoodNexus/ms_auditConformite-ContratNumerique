package tn.esprit.ms_auditConformite_ContratNumerique.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InspectionCaseServiceTest {

    @Mock
    private InspectionCaseRepository repo;

    @Mock
    private RecyclingProductsRepository recyclingProductsRepository;

    @Mock
    private AiInspectionService aiService;

    @InjectMocks
    private InspectionCaseService service;

    private InspectionCase inspectionCase;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        inspectionCase = InspectionCase.builder()
                .caseId(1L)
                .auditorId(100L)
                .delevryTo("DEL123")
                .description("Test description")
                .sanitaryVerdict(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION)
                .build();
                
        mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image data".getBytes());
    }

    @Test
    void scanAndCreate_ShouldReturnSavedInspectionCase() {
        when(aiService.scanImage(any(MultipartFile.class))).thenReturn(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION);
        when(repo.saveAndFlush(any(InspectionCase.class))).thenAnswer(i -> {
            InspectionCase arg = (InspectionCase) i.getArguments()[0];
            arg.setCaseId(2L);
            return arg;
        });

        InspectionCase result = service.scanAndCreate(mockFile, 100L, "DEL123", "Test description");

        assertNotNull(result);
        assertEquals(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION, result.getSanitaryVerdict());
        verify(repo, times(1)).saveAndFlush(any(InspectionCase.class));
    }

    @Test
    void analyseImage_ShouldReturnVerdictMap() {
        when(aiService.scanImage(any(MultipartFile.class))).thenReturn(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION);

        Map<String, Object> result = service.analyseImage(mockFile);

        assertEquals(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION, result.get("sanitaryVerdict"));
        verify(aiService, times(1)).scanImage(any(MultipartFile.class));
    }

    @Test
    void create_ShouldCreateAndSaveRecyclingProductIfDestruction() {
        inspectionCase.setSanitaryVerdict(InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE);
        
        when(repo.saveAndFlush(any(InspectionCase.class))).thenReturn(inspectionCase);
        
        RecyclingProducts mockRecycling = RecyclingProducts.builder().logId(1L).build();
        when(recyclingProductsRepository.saveAndFlush(any(RecyclingProducts.class))).thenReturn(mockRecycling);

        InspectionCase result = service.create(inspectionCase);

        assertNotNull(result);
        assertEquals(InspectionCase.ResolutionStatus.EN_COURS, result.getResolutionStatus());
        verify(recyclingProductsRepository, times(1)).saveAndFlush(any(RecyclingProducts.class));
    }

    @Test
    void getAll_ShouldReturnList() {
        when(repo.findAll()).thenReturn(Arrays.asList(inspectionCase));
        List<InspectionCase> list = service.getAll();
        assertEquals(1, list.size());
    }

    @Test
    void getById_ShouldReturnInspectionCase() {
        when(repo.findById(1L)).thenReturn(Optional.of(inspectionCase));
        assertEquals(inspectionCase, service.getById(1L));
    }

    @Test
    void getById_ShouldThrowExceptionIfNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1L));
    }

    @Test
    void getByAuditor_ShouldReturnList() {
        when(repo.findByAuditorId(100L)).thenReturn(Arrays.asList(inspectionCase));
        assertEquals(1, service.getByAuditor(100L).size());
    }

    @Test
    void getByDelivery_ShouldReturnList() {
        when(repo.findByDelevryTo("DEL123")).thenReturn(Arrays.asList(inspectionCase));
        assertEquals(1, service.getByDelivery("DEL123").size());
    }

    @Test
    void getByStatus_ShouldReturnList() {
        when(repo.findByResolutionStatus(InspectionCase.ResolutionStatus.EN_COURS)).thenReturn(Arrays.asList(inspectionCase));
        assertEquals(1, service.getByStatus(InspectionCase.ResolutionStatus.EN_COURS).size());
    }

    @Test
    void getByVerdict_ShouldReturnList() {
        when(repo.findBySanitaryVerdict(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION)).thenReturn(Arrays.asList(inspectionCase));
        assertEquals(1, service.getByVerdict(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION).size());
    }

    @Test
    void update_ShouldUpdateAndSave() {
        InspectionCase updatedInfo = InspectionCase.builder()
                .description("Updated description")
                .resolutionStatus(InspectionCase.ResolutionStatus.RESOLU)
                .sanitaryVerdict(InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE)
                .auditorId(101L)
                .delevryTo("DEL999")
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(inspectionCase));
        when(repo.save(any(InspectionCase.class))).thenReturn(inspectionCase);

        InspectionCase result = service.update(1L, updatedInfo);

        assertEquals("Updated description", inspectionCase.getDescription());
        assertEquals(InspectionCase.ResolutionStatus.RESOLU, inspectionCase.getResolutionStatus());
        verify(repo).save(inspectionCase);
    }

    @Test
    void updateStatus_ShouldUpdateAndSave() {
        when(repo.findById(1L)).thenReturn(Optional.of(inspectionCase));
        when(repo.save(any(InspectionCase.class))).thenReturn(inspectionCase);

        service.updateStatus(1L, InspectionCase.ResolutionStatus.RESOLU);

        assertEquals(InspectionCase.ResolutionStatus.RESOLU, inspectionCase.getResolutionStatus());
        verify(repo).save(inspectionCase);
    }

    @Test
    void updateVerdict_ShouldUpdateAndSave() {
        when(repo.findById(1L)).thenReturn(Optional.of(inspectionCase));
        when(repo.save(any(InspectionCase.class))).thenReturn(inspectionCase);

        service.updateVerdict(1L, InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION);

        assertEquals(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION, inspectionCase.getSanitaryVerdict());
        verify(repo).save(inspectionCase);
    }

    @Test
    void updateVerdict_ShouldCreateRecyclingProductIfDestructionAndNotExists() {
        when(repo.findById(1L)).thenReturn(Optional.of(inspectionCase));
        when(recyclingProductsRepository.findByInspectionCase_CaseId(1L)).thenReturn(Arrays.asList());
        when(repo.save(any(InspectionCase.class))).thenReturn(inspectionCase);

        service.updateVerdict(1L, InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE);

        verify(recyclingProductsRepository, times(1)).saveAndFlush(any(RecyclingProducts.class));
    }

    @Test
    void delete_ShouldDeleteIfExists() {
        when(repo.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowExceptionIfNotExists() {
        when(repo.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.delete(1L));
        verify(repo, never()).deleteById(anyLong());
    }
}
