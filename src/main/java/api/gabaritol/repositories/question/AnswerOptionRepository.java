package api.gabaritol.repositories.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.question.AnswerOption;
import api.gabaritol.entities.question.Question;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, UUID> {
    List<AnswerOption> findByQuestion(Question question);
}