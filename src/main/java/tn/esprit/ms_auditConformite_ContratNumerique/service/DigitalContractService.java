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

    // CREATE
    public DigitalContract create(DigitalContract contract) {

        // Vérifier si un contrat existe déjà pour cette livraison
        if (repo.findByDeliveryId(contract.getDeliveryId()).isPresent()) {
            throw new RuntimeException(
                    "Contrat déjà généré pour la livraison: "
                            + contract.getDeliveryId()
            );
        }

        contract.setGenerationDate(LocalDate.now());
        contract.setStatus(DigitalContract.ContractStatus.GENERE);
        contract.setPdfDocumentUrl(
                "/contracts/contract_" + contract.getDeliveryId() + ".pdf"
        );

        return repo.save(contract);
    }

    // READ ALL
    public List<DigitalContract> getAll() {
        return repo.findAll();
    }

    // READ BY ID
    public DigitalContract getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Contrat non trouvé: " + id));
    }

    // READ BY DELIVERY
    public DigitalContract getByDelivery(Long deliveryId) {
        return repo.findByDeliveryId(deliveryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Aucun contrat pour la livraison: " + deliveryId));
    }

    // READ BY DONOR
    public List<DigitalContract> getByDonor(Long donorId) {
        return repo.findByDonorId(donorId);
    }

    // READ BY RECEIVER
    public List<DigitalContract> getByReceiver(Long receiverId) {
        return repo.findByReceiverId(receiverId);
    }

    // READ BY STATUS
    public List<DigitalContract> getByStatus(
            DigitalContract.ContractStatus status) {
        return repo.findByStatus(status);
    }

    // UPDATE
    public DigitalContract update(Long id, DigitalContract updated) {
        DigitalContract existing = getById(id);
        existing.setDonorId(updated.getDonorId());
        existing.setReceiverId(updated.getReceiverId());
        existing.setDeliveryId(updated.getDeliveryId());
        existing.setFiscalDeductionValue(updated.getFiscalDeductionValue());
        existing.setPdfDocumentUrl(updated.getPdfDocumentUrl());
        existing.setStatus(updated.getStatus());
        return repo.save(existing);
    }

    // UPDATE STATUS SEULEMENT
    public DigitalContract updateStatus(Long id,
                                        DigitalContract.ContractStatus status) {
        DigitalContract existing = getById(id);
        existing.setStatus(status);
        return repo.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Contrat non trouvé: " + id);
        }
        repo.deleteById(id);
    }
}