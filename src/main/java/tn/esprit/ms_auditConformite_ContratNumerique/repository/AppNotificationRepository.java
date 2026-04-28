package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.AppNotification;
import java.util.List;

public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {
    List<AppNotification> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
