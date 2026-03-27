package tn.esprit.ms_auditConformite_ContratNumerique.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecyclingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private LocalDate transferDate;

    @Enumerated(EnumType.STRING)
    private Destination destination;

    private Double weight;

    // Lié à un InspectionCase
    @ManyToOne
    @JoinColumn(name = "case_id")
    private InspectionCase inspectionCase;

    public enum Destination {
        COMPOST,
        AGRICULTEUR
    }
}