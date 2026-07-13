package api.gabaritol.DTOs.exam;

import java.time.LocalDateTime;
import java.util.UUID;
import api.gabaritol.entities.exam.Difficulty;
import api.gabaritol.entities.exam.ExamStatus;

public record ExamResponseDTO(
    UUID id,
    String title,
    String board,
    String topic,
    String position,
    Difficulty difficulty,
    Integer questionCount,
    ExamStatus status,
    LocalDateTime createdAt
) {
    public static ExamResponseDTO fromEntity(api.gabaritol.entities.exam.Exam exam) {
        return new ExamResponseDTO(
            exam.getId(),
            exam.getTitle(),
            exam.getBoard(),
            exam.getTopic(),
            exam.getPosition(),
            exam.getDifficulty(),
            exam.getQuestionCount(),
            exam.getStatus(),
            exam.getCreatedAt()
        );
    }
}