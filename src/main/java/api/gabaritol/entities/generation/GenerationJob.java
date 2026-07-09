package api.gabaritol.entities.generation;

import java.time.LocalDateTime;
import java.util.UUID;

import api.gabaritol.entities.common.BaseEntity;
import api.gabaritol.entities.exam.Exam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GenerationJob extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Exam exam;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private Integer questionsGenerated;
    private Integer questionsTotal;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
