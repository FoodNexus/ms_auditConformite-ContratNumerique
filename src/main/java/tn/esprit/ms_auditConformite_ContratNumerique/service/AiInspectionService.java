package tn.esprit.ms_auditConformite_ContratNumerique.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.InspectionCase;

import java.util.*;

@Service
public class AiInspectionService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    public InspectionCase.SanitaryVerdict scanImage(MultipartFile image) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

            Map<String, Object> payload = createGeminiPayload(base64Image, image.getContentType());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            String url = GEMINI_API_URL + "?key=" + geminiApiKey;
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

            String resultText = parseGeminiResponse(response);
            
            if (resultText != null && resultText.toUpperCase().contains("DESTRUCTION_RECYCLAGE")) {
                return InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE;
            }
            
            return InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION;

        } catch (Exception e) {
            return new Random().nextInt(100) < 50 ? 
                InspectionCase.SanitaryVerdict.PROPRE_A_LA_CONSOMMATION : 
                InspectionCase.SanitaryVerdict.DESTRUCTION_RECYCLAGE;
        }
    }

    private Map<String, Object> createGeminiPayload(String base64Data, String mimeType) {
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", "Analyze this fruit or vegetable image. If it is fresh, healthy and edible, reply with ONLY 'PROPRE_A_LA_CONSOMMATION'. If it shows signs of rot, mold, severe decay or is not edible, reply with ONLY 'DESTRUCTION_RECYCLAGE'. No other text.");

        Map<String, Object> dataPart = new HashMap<>();
        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mime_type", mimeType);
        inlineData.put("data", base64Data);
        dataPart.put("inline_data", inlineData);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Arrays.asList(textPart, dataPart));

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", Collections.singletonList(content));
        
        return payload;
    }

    private String parseGeminiResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "ERROR";
        }
    }
}
