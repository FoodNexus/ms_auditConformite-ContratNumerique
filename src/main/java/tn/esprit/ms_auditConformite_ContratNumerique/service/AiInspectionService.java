package tn.esprit.ms_auditConformite_ContratNumerique.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;

import java.util.Random;

@Service
public class AiInspectionService {

    /**
     * Mock method to simulate AI scanning of vegetables and fruits.
     * In a real implementation, this would call Google Gemini API.
     */
    public InspectionCase.SanitaryVerdict scanImage(MultipartFile image) {
        // Simulate some processing time
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Logic placeholder:
        // If the filename contains "rotten" or "bad", return DESTRUCTION_RECYCLAGE
        // Otherwise, return a random result (simulating uncertainty)
        String filename = image.getOriginalFilename() != null ? image.getOriginalFilename().toLowerCase() : "";
        
        if (filename.contains("rotten") || filename.contains("bad")) {
            return InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE;
        }
        
        if (filename.contains("fresh") || filename.contains("good")) {
            return InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION;
        }

        // Default: 70% chance of being good (fresh produce is more common)
        return new Random().nextInt(100) < 70 ? 
                InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION : 
                InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE;
    }
}
