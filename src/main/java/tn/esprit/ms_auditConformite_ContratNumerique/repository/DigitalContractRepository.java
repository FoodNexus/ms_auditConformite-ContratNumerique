package tn.esprit.ms_auditConformite_ContratNumerique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms_auditConformite_ContratNumerique.entity.DigitalContract;
import java.util.List;
import java.util.Optional;

public interface DigitalContractRepository
        extends JpaRepository<DigitalContract, Long> {

    Optional<DigitalContract> findByDeliveryId(Long deliveryId);
    List<DigitalContract> findByDonorName(String donorName);
    List<DigitalContract> findByReceiverName(String receiverName);
    List<DigitalContract> findByStatus(DigitalContract.ContractStatus status);
}