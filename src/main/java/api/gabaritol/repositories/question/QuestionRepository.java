package api.gabaritol.repositories.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.exam.Difficulty;
import api.gabaritol.entities.question.Question;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByTopicAndBoardAndDifficultyAndApprovedTrue(
        String topic, String board, Difficulty difficulty
    );

    boolean existsByContentHash(String contentHash);

    long countByTopicAndBoardAndDifficultyAndApprovedTrue(
        String topic, String board, Difficulty difficulty
    );  
}
