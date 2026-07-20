package api.gabaritol.DTOs.question;

import api.gabaritol.entities.question.AnswerOption;

public record AnswerOptionWithAnswerResponseDTO(
    String label, 
    String text, 
    boolean correct
) {
    public static AnswerOptionWithAnswerResponseDTO fromEntity(AnswerOption option) {
        return new AnswerOptionWithAnswerResponseDTO(
            option.getLabel(), 
            option.getText(), 
            option.getCorrect()
        );
    }
}