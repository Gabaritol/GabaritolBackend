package api.gabaritol.DTOs.exam;

import api.gabaritol.entities.exam.Difficulty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateExamRequestDTO(
    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Board is required")
    String board,

    @NotBlank(message = "Topic is required")
    String topic,

    String position, // optional

    @NotNull(message = "Difficulty is required")
    Difficulty difficulty,

    @NotNull(message = "Question count is required")
    @Min(value = 1, message = "Must generate at least 1 question")
    Integer questionCount
) {}