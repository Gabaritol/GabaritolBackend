package api.gabaritol.controllers.user;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.billing.TransactionResponseDTO;
import api.gabaritol.DTOs.user.UserCreditsResponseDTO;
import api.gabaritol.entities.user.User;
import api.gabaritol.services.billing.BillingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final BillingService billingService;

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
}