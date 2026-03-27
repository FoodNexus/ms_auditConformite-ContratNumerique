package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.service.RecyclingProductsService;

import java.util.List;

@RestController
@RequestMapping("/api/recycling-products")
@RequiredArgsConstructor
public class RecyclingProductsController {

    private final RecyclingProductsService service;

    // POST - Créer un produit recyclé lié à un InspectionCase
    @PostMapping("/inspection-case/{caseId}")
    public ResponseEntity<RecyclingProducts> create(
            @PathVariable Long caseId,
            @RequestBody RecyclingProducts product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(caseId, product));
    }

    // GET - Tous les produits recyclés
    @GetMapping
    public List<RecyclingProducts> getAll() {
        return service.getAll();
    }

    // GET - Par ID
    @GetMapping("/{id}")
    public ResponseEntity<RecyclingProducts> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET - Par InspectionCase
    @GetMapping("/inspection-case/{caseId}")
    public List<RecyclingProducts> getByInspectionCase(
            @PathVariable Long caseId) {
        return service.getByInspectionCase(caseId);
    }

    // GET - Par Destination
    @GetMapping("/destination/{destination}")
    public List<RecyclingProducts> getByDestination(
            @PathVariable RecyclingProducts.Destination destination) {
        return service.getByDestination(destination);
    }

    // PUT - Modifier
    @PutMapping("/{id}")
    public ResponseEntity<RecyclingProducts> update(
            @PathVariable Long id,
            @RequestBody RecyclingProducts product) {
        return ResponseEntity.ok(service.update(id, product));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
