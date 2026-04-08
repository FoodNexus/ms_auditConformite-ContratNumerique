package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.service.InspectionCaseService;

import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/inspection-cases")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUDITOR')")
public class InspectionCaseController {

    private final InspectionCaseService service;

    @PostMapping
    public ResponseEntity<InspectionCase> create(
            @RequestBody InspectionCase inspectionCase) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(inspectionCase));
    }

    @GetMapping
    public List<InspectionCase> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionCase> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/auditor/{auditorId}")
    public List<InspectionCase> getByAuditor(@PathVariable Long auditorId) {
        return service.getByAuditor(auditorId);
    }

    @GetMapping("/delivery/{deliveryId}")
    public List<InspectionCase> getByDelivery(@PathVariable Long deliveryId) {
        return service.getByDelivery(deliveryId);
    }

    @GetMapping("/status/{status}")
    public List<InspectionCase> getByStatus(
            @PathVariable InspectionCase.ResolutionStatus status) {
        return service.getByStatus(status);
    }

    @GetMapping("/verdict/{verdict}")
    public List<InspectionCase> getByVerdict(
            @PathVariable InspectionCase.SanitaryVerdict verdict) {
        return service.getByVerdict(verdict);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InspectionCase> update(
            @PathVariable Long id,
            @RequestBody InspectionCase inspectionCase) {
        return ResponseEntity.ok(service.update(id, inspectionCase));
    }

    // PATCH - Modifier le status seulement
    @PatchMapping("/{id}/status")
    public ResponseEntity<InspectionCase> updateStatus(
            @PathVariable Long id,
            @RequestParam InspectionCase.ResolutionStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    // PATCH - Modifier le verdict seulement
    @PatchMapping("/{id}/verdict")
    public ResponseEntity<InspectionCase> updateVerdict(
            @PathVariable Long id,
            @RequestParam InspectionCase.SanitaryVerdict verdict) {
        return ResponseEntity.ok(service.updateVerdict(id, verdict));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}