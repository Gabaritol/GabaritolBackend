package api.gabaritol.DTOs.admin;

import jakarta.validation.constraints.NotNull;

public record AdjustCreditsRequestDTO(
    @NotNull Integer amount
) {}