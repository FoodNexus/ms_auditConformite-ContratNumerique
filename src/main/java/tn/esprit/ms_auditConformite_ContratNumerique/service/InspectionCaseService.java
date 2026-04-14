package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.RecyclingProducts;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.InspectionCaseRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.RecyclingProductsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InspectionCaseService {

    private final InspectionCaseRepository repo;
    private final RecyclingProductsRepository recyclingProductsRepository;
    private final AiInspectionService aiService;

    public InspectionCase scanAndCreate(org.springframework.web.multipart.MultipartFile image, Long auditorId, Long deliveryId, String description) {
        // 1. Simuler l'analyse AI
        InspectionCase.SanitaryVerdict verdict = aiService.scanImage(image);

        // 2. Sauvegarder l'image localement (Simplifié)
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        // Pour ce projet, on stocke juste le nom simulé.
        String imageUrl = "/uploads/" + fileName;

        // 3. Créer l'InspectionCase
        InspectionCase inspectionCase = InspectionCase.builder()
                .auditorId(auditorId)
                .deliveryId(deliveryId)
                .description(description)
                .sanitaryVerdict(verdict)
                .imageUrl(imageUrl)
                .build();

        return create(inspectionCase);
    }

    public java.util.Map<String, Object> analyseImage(org.springframework.web.multipart.MultipartFile image) {
        // 1. Simuler l'analyse AI
        InspectionCase.SanitaryVerdict verdict = aiService.scanImage(image);

        // 2. Sauvegarder l'image localement (Simplifié)
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        String imageUrl = "/uploads/" + fileName;

        // 3. Retourner le verdict et le lien
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("sanitaryVerdict", verdict);
        result.put("imageUrl", imageUrl);
        return result;
    }

    public InspectionCase create(InspectionCase inspectionCase) {
        inspectionCase.setCreationDate(LocalDate.now());
        inspectionCase.setResolutionStatus(
                InspectionCase.ResolutionStatus.EN_COURS
        );

        InspectionCase saved = repo.saveAndFlush(inspectionCase); // ← saveAndFlush

        if (InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE
                .equals(saved.getSanitaryVerdict())) {

            RecyclingProducts recycling = RecyclingProducts.builder()
                    .transferDate(LocalDate.now())
                    .inspectionCase(saved)
                    .weight(null)
                    .destination(null)
                    .build();

            RecyclingProducts savedRecycling =
                    recyclingProductsRepository.saveAndFlush(recycling); // ← saveAndFlush

            System.out.println(" RecyclingProducts créé automatiquement ID: "
                    + savedRecycling.getLogId());
        }

        return saved;
    }

    public List<InspectionCase> getAll() {
        return repo.findAll();
    }

    public InspectionCase getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("InspectionCase non trouvé: " + id));
    }

    public List<InspectionCase> getByAuditor(Long auditorId) {
        return repo.findByAuditorId(auditorId);
    }

    public List<InspectionCase> getByDelivery(Long deliveryId) {
        return repo.findByDeliveryId(deliveryId);
    }

    public List<InspectionCase> getByStatus(
            InspectionCase.ResolutionStatus status) {
        return repo.findByResolutionStatus(status);
    }

    public List<InspectionCase> getByVerdict(
            InspectionCase.SanitaryVerdict verdict) {
        return repo.findBySanitaryVerdict(verdict);
    }

    public InspectionCase update(Long id, InspectionCase updated) {
        InspectionCase existing = getById(id);
        existing.setDescription(updated.getDescription());
        existing.setResolutionStatus(updated.getResolutionStatus());
        existing.setSanitaryVerdict(updated.getSanitaryVerdict());
        existing.setAuditorId(updated.getAuditorId());
        existing.setDeliveryId(updated.getDeliveryId());
        return repo.save(existing);
    }

    public InspectionCase updateStatus(Long id,
                                       InspectionCase.ResolutionStatus status) {
        InspectionCase existing = getById(id);
        existing.setResolutionStatus(status);
        return repo.save(existing);
    }

    public InspectionCase updateVerdict(Long id,
                                        InspectionCase.SanitaryVerdict verdict) {
        InspectionCase existing = getById(id);
        existing.setSanitaryVerdict(verdict);

        if (verdict == InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE) {
            boolean alreadyExists = !recyclingProductsRepository
                    .findByInspectionCase_CaseId(id).isEmpty();

            if (!alreadyExists) {
                RecyclingProducts recycling = RecyclingProducts.builder()
                        .transferDate(LocalDate.now())
                        .inspectionCase(existing)
                        .weight(null)
                        .destination(null)
                        .build();
                recyclingProductsRepository.saveAndFlush(recycling);
            }
        }

        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("InspectionCase non trouvé: " + id);
        }
        repo.deleteById(id);
    }
}