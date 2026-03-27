package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.DigitalContract;
import java.util.List;
import java.util.Optional;

public interface DigitalContractRepository
        extends JpaRepository<DigitalContract, Long> {

    Optional<DigitalContract> findByDeliveryId(Long deliveryId);
    List<DigitalContract> findByDonorId(Long donorId);
    List<DigitalContract> findByReceiverId(Long receiverId);
}