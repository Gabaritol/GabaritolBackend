package api.gabaritol.controllers.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.payment.CheckoutResponseDTO;
import api.gabaritol.DTOs.payment.CreateCheckoutRequestDTO;
import api.gabaritol.entities.user.User;
import api.gabaritol.exceptions.raises.PaymentException;
import api.gabaritol.payment.abacatepay.WebhookSignatureValidator;
import api.gabaritol.payment.abacatepay.DTOs.AbacatePayWebhookPayloadDTO;
import api.gabaritol.services.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentControllerImpl implements PaymentController {

    private final PaymentService paymentService;
    private final WebhookSignatureValidator signatureValidator;
    private final ObjectMapper objectMapper;

    @Value("${abacatepay.webhook.secret}")
    private String expectedWebhookSecret;

    @Override
    public ResponseEntity<CheckoutResponseDTO> createCheckout(CreateCheckoutRequestDTO request, User currentUser) {
        String checkoutUrl = paymentService.createCheckout(currentUser, request.creditPackageId());
        return ResponseEntity.ok(new CheckoutResponseDTO(checkoutUrl));
    }

    @Override
    public ResponseEntity<Void> handleWebhook(String rawBody, String signature, String secretFromQuery) {
        if (!expectedWebhookSecret.equals(secretFromQuery)) {
            log.warn("Webhook rejected: invalid secret in query string.");
            return ResponseEntity.status(401).build();
        }

        if (!signatureValidator.isValid(rawBody, signature)) {
            log.warn("Webhook rejected: invalid HMAC signature.");
            return ResponseEntity.status(401).build();
        }

        try {
            AbacatePayWebhookPayloadDTO payload = objectMapper.readValue(rawBody, AbacatePayWebhookPayloadDTO.class);

            if ("checkout.completed".equals(payload.event())) {
                paymentService.handleWebhookConfirmation(payload.data().id());
            } else {
                log.info("Ignoring unhandled webhook event: {}", payload.event());
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error processing webhook payload", e);
            throw new PaymentException("Failed to process webhook.");
        }
    }
}