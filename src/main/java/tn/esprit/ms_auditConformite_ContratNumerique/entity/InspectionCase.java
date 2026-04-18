package tn.esprit.ms_auditConformite_ContratNumerique.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;

    private LocalDate creationDate;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ResolutionStatus resolutionStatus;

    @Enumerated(EnumType.STRING)
    private SanitaryVerdict sanitaryVerdict;

    private String delevry_to;

    private Long auditorId;


    public enum ResolutionStatus {
        EN_COURS,
        RESOLU,
        FERME
    }

    public enum SanitaryVerdict {
        PROPRE_A_LA_CONSOMMATION,
        DESTRUCTION_RECYCLAGE
    }
}