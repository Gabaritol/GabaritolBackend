package api.gabaritol.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.xml.transform.Source;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;

import api.gabaritol.entities.enums.Difficulty;
import api.gabaritol.entities.enums.ExamStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Exam {
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
    private List<Question> questions;

    @OneToMany(mappedBy = "exam")
    private List<Source> sources;

    private LocalDateTime createdAt;
}