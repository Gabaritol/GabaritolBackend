package api.gabaritol.controllers.admin;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.admin.*;
import api.gabaritol.services.admin.AdminService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    @Override
    public ResponseEntity<CreditCostResponseDTO> createCreditCost(CreateCreditCostRequestDTO request) {
        var cost = adminService.createCreditCost(request.modelName(), request.role(), request.creditCostPerQuestion());
        return ResponseEntity.ok(CreditCostResponseDTO.fromEntity(cost));
    }

    @Override
    public ResponseEntity<List<CreditCostResponseDTO>> listCreditCosts() {
        var costs = adminService.listCreditCosts().stream().map(CreditCostResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(costs);
    }

    @Override
    public ResponseEntity<Void> deactivateCreditCost(UUID id) {
        adminService.deactivateCreditCost(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AdminUserResponseDTO>> listUsers() {
        var users = adminService.listUsers().stream().map(AdminUserResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<AdminUserResponseDTO> adjustUserCredits(UUID id, AdjustCreditsRequestDTO request) {
        var user = adminService.adjustUserCredits(id, request.amount());
        return ResponseEntity.ok(AdminUserResponseDTO.fromEntity(user));
    }

    @Override
    public ResponseEntity<Void> banUser(UUID id) {
        adminService.banUser(id);
        return ResponseEntity.noContent().build();
    }
}