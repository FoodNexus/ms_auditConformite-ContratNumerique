package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms_auditConformite_ContratNumerique.service.AuditStatisticsService;

import java.util.Map;

@RestController
@RequestMapping("/api/audit/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuditStatisticsController {

    private final AuditStatisticsService statisticsService;

    @GetMapping
    public Map<String, Object> getGlobalStats(@RequestParam(required = false) Long auditorId) {
        return statisticsService.getGlobalStats(auditorId);
    }
}
