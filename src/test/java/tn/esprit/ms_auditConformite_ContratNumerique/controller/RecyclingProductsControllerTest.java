package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.service.RecyclingProductsService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecyclingProductsControllerTest {

    @Mock
    private RecyclingProductsService service;

    @InjectMocks
    private RecyclingProductsController controller;

    private RecyclingProducts recyclingProducts;

    @BeforeEach
    void setUp() {
        recyclingProducts = RecyclingProducts.builder().logId(1L).build();
    }

    @Test
    void create_ShouldReturnCreated() {
        when(service.create(1L, recyclingProducts)).thenReturn(recyclingProducts);
        ResponseEntity<RecyclingProducts> response = controller.create(1L, recyclingProducts);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(recyclingProducts, response.getBody());
    }

    @Test
    void getAll_ShouldReturnList() {
        when(service.getAll()).thenReturn(Arrays.asList(recyclingProducts));
        List<RecyclingProducts> list = controller.getAll();
        assertEquals(1, list.size());
    }

    @Test
    void getById_ShouldReturnOk() {
        when(service.getById(1L)).thenReturn(recyclingProducts);
        ResponseEntity<RecyclingProducts> response = controller.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recyclingProducts, response.getBody());
    }

    @Test
    void getByInspectionCase_ShouldReturnList() {
        when(service.getByInspectionCase(1L)).thenReturn(Arrays.asList(recyclingProducts));
        List<RecyclingProducts> list = controller.getByInspectionCase(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getByDestination_ShouldReturnList() {
        when(service.getByDestination(RecyclingProducts.Destination.COMPOST)).thenReturn(Arrays.asList(recyclingProducts));
        List<RecyclingProducts> list = controller.getByDestination(RecyclingProducts.Destination.COMPOST);
        assertEquals(1, list.size());
    }

    @Test
    void update_ShouldReturnOk() {
        when(service.update(1L, recyclingProducts)).thenReturn(recyclingProducts);
        ResponseEntity<RecyclingProducts> response = controller.update(1L, recyclingProducts);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recyclingProducts, response.getBody());
    }

    @Test
    void updateDetails_ShouldReturnOk() {
        when(service.updateDetails(1L, 10.0, RecyclingProducts.Destination.COMPOST)).thenReturn(recyclingProducts);
        ResponseEntity<RecyclingProducts> response = controller.updateDetails(1L, 10.0, RecyclingProducts.Destination.COMPOST);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recyclingProducts, response.getBody());
    }

    @Test
    void delete_ShouldReturnNoContent() {
        ResponseEntity<Void> response = controller.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete(1L);
    }
}
