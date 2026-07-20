package api.gabaritol.DTOs.question;

import java.util.List;
import java.util.UUID;
import api.gabaritol.entities.question.Question;
import api.gabaritol.entities.question.QuestionType;

public record QuestionResponseDTO(
    UUID id,
    String statement,
    QuestionType type,
    List<AnswerOptionResponseDTO> options,
    Integer order
) {
    public static QuestionResponseDTO fromEntity(Question question, List<AnswerOptionResponseDTO> options, Integer order) {
        return new QuestionResponseDTO(
            question.getId(),
            question.getStatement(),
            question.getType(),
            options,
            order
        );
    }
}