package api.gabaritol.entities.exam;

import java.util.List;
import java.util.UUID;

import api.gabaritol.entities.common.BaseEntity;
import api.gabaritol.entities.question.ExamQuestion;
import api.gabaritol.entities.source.Source;
import api.gabaritol.entities.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Exam extends BaseEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    private String title;
    private String board;
    private String topic;
    private String position;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private Integer questionCount;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamQuestion> examQuestions;

    @OneToMany(mappedBy = "exam")
    private List<Source> sources;

}