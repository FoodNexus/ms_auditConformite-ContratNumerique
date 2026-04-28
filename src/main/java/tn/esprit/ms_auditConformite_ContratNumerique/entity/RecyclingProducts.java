package tn.esprit.ms_auditConformite_ContratNumerique.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecyclingProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private LocalDateTime transferDate;

    @Column(nullable = true)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private InspectionCase inspectionCase;

    public enum Destination {
        COMPOST,
        AGRICULTEUR
    }
}