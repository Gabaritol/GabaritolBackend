package api.gabaritol.DTOs.question;

import api.gabaritol.entities.question.AnswerOption;

public record AnswerOptionResponseDTO(String label, String text) {
    public static AnswerOptionResponseDTO fromEntity(AnswerOption option) {
        return new AnswerOptionResponseDTO(option.getLabel(), option.getText());
    }
}