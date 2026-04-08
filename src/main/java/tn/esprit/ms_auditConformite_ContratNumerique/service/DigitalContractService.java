package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.DigitalContract;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.DigitalContractRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DigitalContractService {

    private final DigitalContractRepository repo;

    public DigitalContract create(DigitalContract contract) {

        if (repo.findByDeliveryId(contract.getDeliveryId()).isPresent()) {
            throw new RuntimeException(
                    "Contrat déjà généré pour la livraison: "
                            + contract.getDeliveryId()
            );
        }

        contract.setGenerationDate(LocalDate.now());
        contract.setStatus(DigitalContract.ContractStatus.GENERE);
        contract = repo.save(contract);
        
        contract.setPdfDocumentUrl(
                "/audit/contracts/print/" + contract.getContractId()
        );

        return repo.save(contract);
    }

    public List<DigitalContract> getAll() {
        return repo.findAll();
    }

    public DigitalContract getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Contrat non trouvé: " + id));
    }

    public DigitalContract getByDelivery(Long deliveryId) {
        return repo.findByDeliveryId(deliveryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Aucun contrat pour la livraison: " + deliveryId));
    }

    public List<DigitalContract> getByDonorName(String donorName) {
        return repo.findByDonorName(donorName);
    }

    public List<DigitalContract> getByReceiverName(String receiverName) {
        return repo.findByReceiverName(receiverName);
    }

    public List<DigitalContract> getByStatus(
            DigitalContract.ContractStatus status) {
        return repo.findByStatus(status);
    }

    public DigitalContract update(Long id, DigitalContract updated) {
        DigitalContract existing = getById(id);
        existing.setDonorName(updated.getDonorName());
        existing.setReceiverName(updated.getReceiverName());
        existing.setDeliveryId(updated.getDeliveryId());
        existing.setFiscalDeductionValue(updated.getFiscalDeductionValue());
        existing.setPdfDocumentUrl(updated.getPdfDocumentUrl());
        existing.setStatus(updated.getStatus());
        return repo.save(existing);
    }

    public DigitalContract updateStatus(Long id,
                                        DigitalContract.ContractStatus status) {
        DigitalContract existing = getById(id);
        existing.setStatus(status);
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Contrat non trouvé: " + id);
        }
        repo.deleteById(id);
    }
}