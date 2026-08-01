package api.gabaritol.DTOs.admin;

import java.time.LocalDateTime;
import java.util.UUID;
import api.gabaritol.entities.user.PlanType;
import api.gabaritol.entities.user.Role;
import api.gabaritol.entities.user.User;

public record AdminUserResponseDTO(
    UUID id, 
    String username, 
    String email, 
    Role role, 
    PlanType plan,
    Integer availableCredits, 
    Boolean verified, 
    Boolean deleted, 
    LocalDateTime createdAt
) {
    public static AdminUserResponseDTO fromEntity(User user) {
        return new AdminUserResponseDTO(
            user.getId(), 
            user.getUsername(), 
            user.getEmail(), 
            user.getRole(),
            user.getPlan(), 
            user.getAvailableCredits(), 
            user.isVerified(),
            user.isDeleted(), 
            user.getCreatedAt()
        );
    }
}