package tn.esprit.ms_auditConformite_ContratNumerique.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.AppNotification;
import tn.esprit.ms_auditConformite_ContratNumerique.repository.AppNotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppNotificationService {

    private final AppNotificationRepository repo;

    public void createNotification(String message, String type) {
        AppNotification notif = AppNotification.builder()
                .message(message)
                .type(type)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        repo.save(notif);
    }

    public List<AppNotification> getUnread() {
        return repo.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public void markAsRead(Long id) {
        repo.findById(id).ifPresent(n -> {
            n.setRead(true);
            repo.save(n);
        });
    }

    public void markAllAsRead() {
        List<AppNotification> unread = repo.findByIsReadFalseOrderByCreatedAtDesc();
        unread.forEach(n -> n.setRead(true));
        repo.saveAll(unread);
    }
}
