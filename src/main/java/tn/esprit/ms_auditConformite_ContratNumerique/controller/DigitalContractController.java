package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.DigitalContract;
import tn.esprit.ms_auditConformite_ContratNumerique.service.DigitalContractService;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class DigitalContractController {

    private final DigitalContractService service;

    // POST - Créer un contrat
    @PostMapping
    public ResponseEntity<DigitalContract> create(
            @RequestBody DigitalContract contract) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(contract));
    }

    // GET - Tous les contrats
    @GetMapping
    public List<DigitalContract> getAll() {
        return service.getAll();
    }

    // GET - Par ID
    @GetMapping("/{id}")
    public ResponseEntity<DigitalContract> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET - Par Delivery
    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<DigitalContract> getByDelivery(
            @PathVariable Long deliveryId) {
        return ResponseEntity.ok(service.getByDelivery(deliveryId));
    }

    // GET - Par Donor
    @GetMapping("/donor/{donorId}")
    public List<DigitalContract> getByDonor(@PathVariable Long donorId) {
        return service.getByDonor(donorId);
    }

    // GET - Par Receiver
    @GetMapping("/receiver/{receiverId}")
    public List<DigitalContract> getByReceiver(@PathVariable Long receiverId) {
        return service.getByReceiver(receiverId);
    }

    // GET - Par Status
    @GetMapping("/status/{status}")
    public List<DigitalContract> getByStatus(
            @PathVariable DigitalContract.ContractStatus status) {
        return service.getByStatus(status);
    }

    // PUT - Modifier tout
    @PutMapping("/{id}")
    public ResponseEntity<DigitalContract> update(
            @PathVariable Long id,
            @RequestBody DigitalContract contract) {
        return ResponseEntity.ok(service.update(id, contract));
    }

    // PATCH - Modifier status seulement
    @PatchMapping("/{id}/status")
    public ResponseEntity<DigitalContract> updateStatus(
            @PathVariable Long id,
            @RequestParam DigitalContract.ContractStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}