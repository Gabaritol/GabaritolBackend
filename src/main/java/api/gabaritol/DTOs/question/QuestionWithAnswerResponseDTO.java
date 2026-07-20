package api.gabaritol.DTOs.question;

import java.util.List;
import java.util.UUID;
import api.gabaritol.entities.question.Question;
import api.gabaritol.entities.question.QuestionType;

public record QuestionWithAnswerResponseDTO(
    UUID id,
    String statement,
    QuestionType type,
    List<AnswerOptionWithAnswerResponseDTO> options,
    String explanation,
    Integer order
) {
    public static QuestionWithAnswerResponseDTO fromEntity(
        Question question, 
        List<AnswerOptionWithAnswerResponseDTO> options, 
        Integer order
    ) {
        return new QuestionWithAnswerResponseDTO(
            question.getId(),
            question.getStatement(),
            question.getType(),
            options,
            question.getExplanation(),
            order
        );
    }
}