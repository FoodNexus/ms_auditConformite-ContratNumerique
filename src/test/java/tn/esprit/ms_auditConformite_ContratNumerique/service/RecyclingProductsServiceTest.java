package tn.esprit.ms_auditConformite_ContratNumerique.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecyclingProductsServiceTest {

    @Mock
    private RecyclingProductsRepository repo;

    @Mock
    private InspectionCaseRepository inspectionCaseRepository;

    @InjectMocks
    private RecyclingProductsService service;

    private RecyclingProducts recyclingProducts;
    private InspectionCase inspectionCase;

    @BeforeEach
    void setUp() {
        inspectionCase = InspectionCase.builder()
                .caseId(1L)
                .sanitaryVerdict(InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE)
                .build();

        recyclingProducts = RecyclingProducts.builder()
                .logId(1L)
                .inspectionCase(inspectionCase)
                .weight(10.5)
                .destination(RecyclingProducts.Destination.COMPOST)
                .build();
    }

    @Test
    void create_ShouldSaveRecyclingProduct() {
        when(inspectionCaseRepository.findById(1L)).thenReturn(Optional.of(inspectionCase));
        when(repo.save(any(RecyclingProducts.class))).thenReturn(recyclingProducts);

        RecyclingProducts newProduct = new RecyclingProducts();
        RecyclingProducts result = service.create(1L, newProduct);

        assertNotNull(result);
        assertEquals(inspectionCase, newProduct.getInspectionCase());
        assertNotNull(newProduct.getTransferDate());
        verify(repo).save(newProduct);
    }

    @Test
    void create_ShouldThrowExceptionIfVerdictNotDestruction() {
        inspectionCase.setSanitaryVerdict(InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION);
        when(inspectionCaseRepository.findById(1L)).thenReturn(Optional.of(inspectionCase));

        assertThrows(RuntimeException.class, () -> service.create(1L, new RecyclingProducts()));
        verify(repo, never()).save(any(RecyclingProducts.class));
    }

    @Test
    void getAll_ShouldReturnList() {
        when(repo.findAll()).thenReturn(Arrays.asList(recyclingProducts));
        assertEquals(1, service.getAll().size());
    }

    @Test
    void getById_ShouldReturnProduct() {
        when(repo.findById(1L)).thenReturn(Optional.of(recyclingProducts));
        assertEquals(recyclingProducts, service.getById(1L));
    }

    @Test
    void getById_ShouldThrowExceptionIfNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1L));
    }

    @Test
    void getByInspectionCase_ShouldReturnList() {
        when(repo.findByInspectionCase_CaseId(1L)).thenReturn(Arrays.asList(recyclingProducts));
        assertEquals(1, service.getByInspectionCase(1L).size());
    }

    @Test
    void getByDestination_ShouldReturnList() {
        when(repo.findByDestination(RecyclingProducts.Destination.COMPOST)).thenReturn(Arrays.asList(recyclingProducts));
        assertEquals(1, service.getByDestination(RecyclingProducts.Destination.COMPOST).size());
    }

    @Test
    void update_ShouldUpdateAndSave() {
        RecyclingProducts updated = RecyclingProducts.builder()
                .weight(20.0)
                .destination(RecyclingProducts.Destination.AGRICULTEUR)
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(recyclingProducts));
        when(repo.save(any(RecyclingProducts.class))).thenReturn(recyclingProducts);

        RecyclingProducts result = service.update(1L, updated);

        assertEquals(20.0, recyclingProducts.getWeight());
        assertEquals(RecyclingProducts.Destination.AGRICULTEUR, recyclingProducts.getDestination());
        verify(repo).save(recyclingProducts);
    }

    @Test
    void updateDetails_ShouldUpdateAndSave() {
        when(repo.findById(1L)).thenReturn(Optional.of(recyclingProducts));
        when(repo.save(any(RecyclingProducts.class))).thenReturn(recyclingProducts);

        service.updateDetails(1L, 30.0, RecyclingProducts.Destination.AGRICULTEUR);

        assertEquals(30.0, recyclingProducts.getWeight());
        assertEquals(RecyclingProducts.Destination.AGRICULTEUR, recyclingProducts.getDestination());
        verify(repo).save(recyclingProducts);
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
