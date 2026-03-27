package tn.esprit.ms_auditConformite_ContratNumerique.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    private LocalDate generationDate;

    private String pdfDocumentUrl;

    private Double fiscalDeductionValue;

    // Référence vers la Delivery (dans un autre microservice)
    private Long deliveryId;

    // Référence vers le Donor (dans ms_gestionUser)
    private Long donorId;

    // Référence vers le Receiver (dans ms_gestionUser)
    private Long receiverId;

    // Enums
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    public enum ContractStatus {
        GENERE,
        ENVOYE,
        ARCHIVE
    }
}