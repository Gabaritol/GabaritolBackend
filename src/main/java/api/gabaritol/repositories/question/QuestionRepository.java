package api.gabaritol.repositories.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import api.gabaritol.entities.exam.Difficulty;
import api.gabaritol.entities.question.Question;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findByTopicAndBoardAndDifficultyAndApprovedTrueOrderByTimesUsedAsc(
        String topic, 
        String board, 
        Difficulty difficulty, 
        Pageable pageable
    );

    boolean existsByContentHash(String contentHash);

    long countByTopicAndBoardAndDifficultyAndApprovedTrue(
        String topic, 
        String board, 
        Difficulty difficulty
    );
}
