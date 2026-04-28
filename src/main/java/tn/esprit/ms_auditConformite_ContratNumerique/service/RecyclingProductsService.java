package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecyclingProductsService {

    private final RecyclingProductsRepository repo;
    private final InspectionCaseRepository inspectionCaseRepository;

    public RecyclingProducts create(Long caseId, RecyclingProducts product) {

        InspectionCase inspectionCase = inspectionCaseRepository
                .findById(caseId)
                .orElseThrow(() ->
                        new RuntimeException("InspectionCase non trouvé: " + caseId));

        if (inspectionCase.getSanitaryVerdict() !=
                InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE) {
            throw new RuntimeException(
                    "Le produit doit avoir le verdict DESTRUCTION_RECYCLAGE " +
                            "pour être recyclé"
            );
        }

        product.setTransferDate(LocalDateTime.now());
        product.setInspectionCase(inspectionCase);
        return repo.save(product);
    }

    public List<RecyclingProducts> getAll() {
        return repo.findAll();
    }

    public RecyclingProducts getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("RecyclingProducts non trouvé: " + id));
    }

    public List<RecyclingProducts> getByInspectionCase(Long caseId) {
        return repo.findByInspectionCase_CaseId(caseId);
    }

    public List<RecyclingProducts> getByDestination(
            RecyclingProducts.Destination destination) {
        return repo.findByDestination(destination);
    }

    public RecyclingProducts update(Long id, RecyclingProducts updated) {
        RecyclingProducts existing = getById(id);
        existing.setWeight(updated.getWeight());
        existing.setDestination(updated.getDestination());
        existing.setTransferDate(updated.getTransferDate());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("RecyclingProducts non trouvé: " + id);
        }
        repo.deleteById(id);
    }

    public RecyclingProducts updateDetails(Long id,
                                           Double weight,
                                           RecyclingProducts.Destination destination) {
        RecyclingProducts existing = getById(id);
        existing.setWeight(weight);
        existing.setDestination(destination);
        return repo.save(existing);
    }
}