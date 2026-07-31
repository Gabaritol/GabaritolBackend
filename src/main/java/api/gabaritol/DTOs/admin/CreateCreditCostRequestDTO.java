package api.gabaritol.DTOs.admin;

import api.gabaritol.entities.billing.AIRole;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCreditCostRequestDTO(
    @NotBlank String modelName,
    @NotNull AIRole role,
    @NotNull @Min(1) Integer creditCostPerQuestion
) {}