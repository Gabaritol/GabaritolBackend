package api.gabaritol.DTOs.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginCodeRequestDTO(
    @NotBlank @Email
    String email
) {}