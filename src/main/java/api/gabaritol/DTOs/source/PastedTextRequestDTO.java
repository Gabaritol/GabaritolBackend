package api.gabaritol.DTOs.source;

import jakarta.validation.constraints.NotBlank;

public record PastedTextRequestDTO(
    @NotBlank(message = "Text is required")
    String text
) {}