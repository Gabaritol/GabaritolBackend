package api.gabaritol.DTOs.user;

public record UserCreditsResponseDTO(
    Integer availableCredits,
    Integer maxCap
) {}