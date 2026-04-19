package tn.esprit.ms_auditConformite_ContratNumerique.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;

import java.util.Map;

@Service
public class AiInspectionService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // URL for our local Python AI service
    private static final String PYTHON_AI_URL = "http://localhost:8000/predict";

    public InspectionCase.SanitaryVerdict scanImage(MultipartFile image) {
        try {
            // Prepare multipart request for the Python service
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            body.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Call Python Service
            ResponseEntity<Map> response = restTemplate.postForEntity(PYTHON_AI_URL, requestEntity, Map.class);
            Map<String, Object> result = response.getBody();

            if (result != null) {
                String className = (String) result.get("class_name");
                double confidence = (double) result.get("confidence");
                
                System.out.println("AI Prediction: " + className + " (Confidence: " + confidence + ")");
                
                // If the dynamic class name contains "Rotten", we flag for destruction/recycling
                if (className != null && className.toLowerCase().contains("rotten")) {
                     return InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE;
                }
            }
            
            return InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION;

        } catch (Exception e) {
            System.err.println("Error calling local AI service: " + e.getMessage());
            // Fallback to safe verdict
            return InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION;
        }
    }
}
