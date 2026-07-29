package api.gabaritol.controllers.user;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.billing.TransactionResponseDTO;
import api.gabaritol.DTOs.user.UserCreditsResponseDTO;
import api.gabaritol.entities.user.User;
import api.gabaritol.services.billing.BillingService;
import api.gabaritol.services.user.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final BillingService billingService;
    private final UserService userService;

    @Override
    public ResponseEntity<UserCreditsResponseDTO> getCredits(User currentUser) {
        return ResponseEntity.ok(billingService.getUserCredits(currentUser));
    }

    @Override
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(User currentUser) {
        List<TransactionResponseDTO> transactions = billingService.getUserTransactions(currentUser).stream()
            .map(TransactionResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(transactions);
    }

    @Override
    public ResponseEntity<Void> deleteAccount(User currentUser) {
        userService.deleteAccount(currentUser);
        return ResponseEntity.noContent().build();
    }
}