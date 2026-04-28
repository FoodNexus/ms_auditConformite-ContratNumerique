package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.DigitalContract;
import tn.esprit.ms_auditConformite_ContratNumerique.service.DigitalContractService;

import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DigitalContractController {

    private final DigitalContractService service;

    @PostMapping
    public ResponseEntity<DigitalContract> create(
            @RequestBody DigitalContract contract) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(contract));
    }

    @GetMapping
    public List<DigitalContract> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DigitalContract> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/delivery/{delevry_to}")
    public ResponseEntity<DigitalContract> getByDelivery(
            @PathVariable String delevry_to) {
        return ResponseEntity.ok(service.getByDelivery(delevry_to));
    }

    @GetMapping("/donor/{donorName}")
    public List<DigitalContract> getByDonor(@PathVariable String donorName) {
        return service.getByDonorName(donorName);
    }

    @GetMapping("/receiver/{receiverName}")
    public List<DigitalContract> getByReceiver(@PathVariable String receiverName) {
        return service.getByReceiverName(receiverName);
    }

    @GetMapping("/status/{status}")
    public List<DigitalContract> getByStatus(
            @PathVariable DigitalContract.ContractStatus status) {
        return service.getByStatus(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DigitalContract> update(
            @PathVariable Long id,
            @RequestBody DigitalContract contract) {
        return ResponseEntity.ok(service.update(id, contract));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DigitalContract> updateStatus(
            @PathVariable Long id,
            @RequestParam DigitalContract.ContractStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}