package tn.esprit.ms_auditConformite_ContratNumerique.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.AppNotification;
import tn.esprit.ms_auditConformite_ContratNumerique.service.AppNotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // For local dev
public class AppNotificationController {

    private final AppNotificationService service;

    @GetMapping
    public List<AppNotification> getAll() {
        return service.getAll();
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        service.markAllAsRead();
        return ResponseEntity.noContent().build();
    }
}
