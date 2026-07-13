package api.gabaritol.DTOs.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginPasswordRequestDTO(
    @NotBlank 
    @Email
    String email,

    @NotBlank
    String password
) {}