package api.gabaritol.entities.question;

import java.util.UUID;
import api.gabaritol.entities.common.BaseEntity;
import api.gabaritol.entities.exam.Exam;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ExamQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Question question;

    private Integer orderInExam;
}