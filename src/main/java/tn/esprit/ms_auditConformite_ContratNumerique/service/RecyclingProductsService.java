package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecyclingProductsService {

    private final RecyclingProductsRepository repo;
    private final InspectionCaseRepository inspectionCaseRepository;

    // CREATE
    public RecyclingProducts create(Long caseId, RecyclingProducts product) {

        // Vérifier que l'InspectionCase existe
        InspectionCase inspectionCase = inspectionCaseRepository
                .findById(caseId)
                .orElseThrow(() ->
                        new RuntimeException("InspectionCase non trouvé: " + caseId));

        // Vérifier que le verdict est bien DESTRUCTION_RECYCLAGE
        if (inspectionCase.getSanitaryVerdict() !=
                InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE) {
            throw new RuntimeException(
                    "Le produit doit avoir le verdict DESTRUCTION_RECYCLAGE " +
                            "pour être recyclé"
            );
        }

        product.setTransferDate(LocalDate.now());
        product.setInspectionCase(inspectionCase);
        return repo.save(product);
    }

    // READ ALL
    public List<RecyclingProducts> getAll() {
        return repo.findAll();
    }

    // READ BY ID
    public RecyclingProducts getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("RecyclingProducts non trouvé: " + id));
    }

    // READ BY INSPECTION CASE
    public List<RecyclingProducts> getByInspectionCase(Long caseId) {
        return repo.findByInspectionCase_CaseId(caseId);
    }

    // READ BY DESTINATION
    public List<RecyclingProducts> getByDestination(
            RecyclingProducts.Destination destination) {
        return repo.findByDestination(destination);
    }

    // UPDATE
    public RecyclingProducts update(Long id, RecyclingProducts updated) {
        RecyclingProducts existing = getById(id);
        existing.setWeight(updated.getWeight());
        existing.setDestination(updated.getDestination());
        existing.setTransferDate(updated.getTransferDate());
        return repo.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("RecyclingProducts non trouvé: " + id);
        }
        repo.deleteById(id);
    }

    // UPDATE WEIGHT ET DESTINATION SEULEMENT
    public RecyclingProducts updateDetails(Long id,
                                           Double weight,
                                           RecyclingProducts.Destination destination) {
        RecyclingProducts existing = getById(id);
        existing.setWeight(weight);
        existing.setDestination(destination);
        return repo.save(existing);
    }
}