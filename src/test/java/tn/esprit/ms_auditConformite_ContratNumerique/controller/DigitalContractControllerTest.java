package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.DigitalContract;
import tn.esprit.ms_auditConformite_ContratNumerique.service.DigitalContractService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DigitalContractControllerTest {

    @Mock
    private DigitalContractService service;

    @InjectMocks
    private DigitalContractController controller;

    private DigitalContract contract;

    @BeforeEach
    void setUp() {
        contract = DigitalContract.builder().contractId(1L).build();
    }

    @Test
    void create_ShouldReturnCreated() {
        when(service.create(contract)).thenReturn(contract);
        ResponseEntity<DigitalContract> response = controller.create(contract);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(contract, response.getBody());
    }

    @Test
    void getAll_ShouldReturnList() {
        when(service.getAll()).thenReturn(Arrays.asList(contract));
        List<DigitalContract> list = controller.getAll();
        assertEquals(1, list.size());
    }

    @Test
    void getById_ShouldReturnOk() {
        when(service.getById(1L)).thenReturn(contract);
        ResponseEntity<DigitalContract> response = controller.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contract, response.getBody());
    }

    @Test
    void getByDelivery_ShouldReturnOk() {
        when(service.getByDelivery("DEL123")).thenReturn(contract);
        ResponseEntity<DigitalContract> response = controller.getByDelivery("DEL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contract, response.getBody());
    }

    @Test
    void getByDonor_ShouldReturnList() {
        when(service.getByDonorName("Donor A")).thenReturn(Arrays.asList(contract));
        List<DigitalContract> list = controller.getByDonor("Donor A");
        assertEquals(1, list.size());
    }

    @Test
    void getByReceiver_ShouldReturnList() {
        when(service.getByReceiverName("Receiver A")).thenReturn(Arrays.asList(contract));
        List<DigitalContract> list = controller.getByReceiver("Receiver A");
        assertEquals(1, list.size());
    }

    @Test
    void getByStatus_ShouldReturnList() {
        when(service.getByStatus(DigitalContract.ContractStatus.GENERE)).thenReturn(Arrays.asList(contract));
        List<DigitalContract> list = controller.getByStatus(DigitalContract.ContractStatus.GENERE);
        assertEquals(1, list.size());
    }

    @Test
    void update_ShouldReturnOk() {
        when(service.update(1L, contract)).thenReturn(contract);
        ResponseEntity<DigitalContract> response = controller.update(1L, contract);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contract, response.getBody());
    }

    @Test
    void updateStatus_ShouldReturnOk() {
        when(service.updateStatus(1L, DigitalContract.ContractStatus.ENVOYE)).thenReturn(contract);
        ResponseEntity<DigitalContract> response = controller.updateStatus(1L, DigitalContract.ContractStatus.ENVOYE);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contract, response.getBody());
    }

    @Test
    void delete_ShouldReturnNoContent() {
        ResponseEntity<Void> response = controller.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete(1L);
    }
}
