package tn.esprit.ms_auditConformite_ContratNumerique.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "delevry_to")
    @JsonProperty("delevry_to")
    private String delevryTo;

    private String donorName;

    private String receiverName;

    // Enums
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    public enum ContractStatus {
        GENERE,
        ENVOYE,
        ARCHIVE
    }
}