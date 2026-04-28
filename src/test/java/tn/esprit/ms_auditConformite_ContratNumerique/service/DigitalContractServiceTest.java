package tn.esprit.ms_auditConformite_ContratNumerique.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.DigitalContract;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.DigitalContractRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DigitalContractServiceTest {

    @Mock
    private DigitalContractRepository repo;

    @InjectMocks
    private DigitalContractService service;

    private DigitalContract contract;

    @BeforeEach
    void setUp() {
        contract = DigitalContract.builder()
                .contractId(1L)
                .delevryTo("DEL123")
                .donorName("Donor A")
                .receiverName("Receiver A")
                .build();
    }

    @Test
    void create_ShouldThrowExceptionIfDeliveryExists() {
        when(repo.findByDelevryTo("DEL123")).thenReturn(Optional.of(contract));

        assertThrows(RuntimeException.class, () -> service.create(contract));
        verify(repo, never()).save(any(DigitalContract.class));
    }

    @Test
    void create_ShouldSaveAndSetStatus() {
        when(repo.findByDelevryTo("DEL123")).thenReturn(Optional.empty());
        when(repo.save(any(DigitalContract.class))).thenReturn(contract);

        DigitalContract result = service.create(contract);

        assertNotNull(result);
        assertEquals(DigitalContract.ContractStatus.GENERE, contract.getStatus());
        assertNotNull(contract.getGenerationDate());
        assertEquals("/audit/contracts/print/1", contract.getPdfDocumentUrl());
        verify(repo, times(2)).save(any(DigitalContract.class));
    }

    @Test
    void getAll_ShouldReturnList() {
        when(repo.findAll()).thenReturn(Arrays.asList(contract));
        List<DigitalContract> list = service.getAll();
        assertEquals(1, list.size());
    }

    @Test
    void getById_ShouldReturnContract() {
        when(repo.findById(1L)).thenReturn(Optional.of(contract));
        assertEquals(contract, service.getById(1L));
    }

    @Test
    void getById_ShouldThrowExceptionIfNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1L));
    }

    @Test
    void getByDelivery_ShouldReturnContract() {
        when(repo.findByDelevryTo("DEL123")).thenReturn(Optional.of(contract));
        assertEquals(contract, service.getByDelivery("DEL123"));
    }

    @Test
    void getByDelivery_ShouldThrowExceptionIfNotFound() {
        when(repo.findByDelevryTo("DEL123")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getByDelivery("DEL123"));
    }

    @Test
    void getByDonorName_ShouldReturnList() {
        when(repo.findByDonorName("Donor A")).thenReturn(Arrays.asList(contract));
        assertEquals(1, service.getByDonorName("Donor A").size());
    }

    @Test
    void getByReceiverName_ShouldReturnList() {
        when(repo.findByReceiverName("Receiver A")).thenReturn(Arrays.asList(contract));
        assertEquals(1, service.getByReceiverName("Receiver A").size());
    }

    @Test
    void getByStatus_ShouldReturnList() {
        when(repo.findByStatus(DigitalContract.ContractStatus.GENERE)).thenReturn(Arrays.asList(contract));
        assertEquals(1, service.getByStatus(DigitalContract.ContractStatus.GENERE).size());
    }

    @Test
    void update_ShouldUpdateAndSave() {
        DigitalContract updatedInfo = DigitalContract.builder()
                .donorName("New Donor")
                .receiverName("New Receiver")
                .delevryTo("NEW_DEL")
                .fiscalDeductionValue(100.0)
                .pdfDocumentUrl("/new/url")
                .status(DigitalContract.ContractStatus.ENVOYE)
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(contract));
        when(repo.save(any(DigitalContract.class))).thenReturn(contract);

        DigitalContract result = service.update(1L, updatedInfo);

        assertEquals("New Donor", contract.getDonorName());
        assertEquals(DigitalContract.ContractStatus.ENVOYE, contract.getStatus());
        verify(repo).save(contract);
    }

    @Test
    void updateStatus_ShouldUpdateAndSave() {
        when(repo.findById(1L)).thenReturn(Optional.of(contract));
        when(repo.save(any(DigitalContract.class))).thenReturn(contract);

        service.updateStatus(1L, DigitalContract.ContractStatus.ENVOYE);

        assertEquals(DigitalContract.ContractStatus.ENVOYE, contract.getStatus());
        verify(repo).save(contract);
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
