package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.service.RecyclingProductsService;

import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/recycling-products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUDITOR')")
public class RecyclingProductsController {

    private final RecyclingProductsService service;

    @PostMapping("/inspection-case/{caseId}")
    public ResponseEntity<RecyclingProducts> create(
            @PathVariable Long caseId,
            @RequestBody RecyclingProducts product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(caseId, product));
    }

    @GetMapping
    public List<RecyclingProducts> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecyclingProducts> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/inspection-case/{caseId}")
    public List<RecyclingProducts> getByInspectionCase(
            @PathVariable Long caseId) {
        return service.getByInspectionCase(caseId);
    }

    @GetMapping("/destination/{destination}")
    public List<RecyclingProducts> getByDestination(
            @PathVariable RecyclingProducts.Destination destination) {
        return service.getByDestination(destination);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecyclingProducts> update(
            @PathVariable Long id,
            @RequestBody RecyclingProducts product) {
        return ResponseEntity.ok(service.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/details")
    public ResponseEntity<RecyclingProducts> updateDetails(
            @PathVariable Long id,
            @RequestParam Double weight,
            @RequestParam RecyclingProducts.Destination destination) {
        return ResponseEntity.ok(
                service.updateDetails(id, weight, destination));
    }
}
