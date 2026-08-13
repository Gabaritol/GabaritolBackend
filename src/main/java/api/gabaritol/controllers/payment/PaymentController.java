package api.gabaritol.controllers.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import api.gabaritol.DTOs.payment.CreateCheckoutRequestDTO;
import api.gabaritol.DTOs.payment.CheckoutResponseDTO;
import api.gabaritol.entities.user.User;
import jakarta.validation.Valid;

public interface PaymentController {

    @PostMapping("/api/payments/checkout")
    ResponseEntity<CheckoutResponseDTO> createCheckout(
        @Valid @RequestBody CreateCheckoutRequestDTO request,
        @AuthenticationPrincipal User currentUser
    );

    @PostMapping("/api/webhooks/abacatepay")
    ResponseEntity<Void> handleWebhook(
        @RequestBody String rawPayload,
        @RequestHeader("X-Webhook-Signature") String signature
    );
}