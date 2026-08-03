package api.gabaritol.controllers.admin;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import api.gabaritol.DTOs.admin.*;
import jakarta.validation.Valid;

@RequestMapping("/api/admin")
public interface AdminController {
    @PostMapping("/credit-costs")
    ResponseEntity<CreditCostResponseDTO> createCreditCost(
        @Valid @RequestBody CreateCreditCostRequestDTO request
    );

    @GetMapping("/credit-costs")
    ResponseEntity<List<CreditCostResponseDTO>> listCreditCosts();

    @DeleteMapping("/credit-costs/{id}")
    ResponseEntity<Void> deactivateCreditCost(@PathVariable UUID id);

    @GetMapping("/users")
    ResponseEntity<List<AdminUserResponseDTO>> listUsers();

    @PatchMapping("/users/{id}/credits")
    ResponseEntity<AdminUserResponseDTO> adjustUserCredits(
        @PathVariable UUID id, 
        @Valid @RequestBody AdjustCreditsRequestDTO request
    );

    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> banUser(@PathVariable UUID id);
}