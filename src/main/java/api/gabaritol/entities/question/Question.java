package api.gabaritol.entities.question;

import java.util.List;
import java.util.UUID;

import api.gabaritol.entities.common.BaseEntity;
import api.gabaritol.entities.exam.Difficulty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "questions")
public class Question extends BaseEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String statement;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerOption> options;

    private String correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private String topic;
    private String board;
    private String position;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String generatorModel;
    private String sourceReference;

    private Boolean reviewed;
    private Boolean approved;

    private Integer timesUsed;
    private String contentHash;
}