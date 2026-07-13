package api.gabaritol.DTOs.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyCodeRequestDTO(
    @NotBlank @Email
    String email,

    @NotBlank
    @Size(min = 5, max = 5, message = "Code must be 5 characters")
    String code 
) {}