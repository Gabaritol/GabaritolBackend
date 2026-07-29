package api.gabaritol.controllers.user;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import api.gabaritol.DTOs.billing.TransactionResponseDTO;
import api.gabaritol.DTOs.user.UserCreditsResponseDTO;
import api.gabaritol.entities.user.User;

@RequestMapping("/api/users/me")
public interface UserController {

    @GetMapping("/credits")
    ResponseEntity<UserCreditsResponseDTO> getCredits(@AuthenticationPrincipal User currentUser);

    @GetMapping("/transactions")
    ResponseEntity<List<TransactionResponseDTO>> getTransactions(@AuthenticationPrincipal User currentUser);

    @DeleteMapping
    ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal User currentUser);
}