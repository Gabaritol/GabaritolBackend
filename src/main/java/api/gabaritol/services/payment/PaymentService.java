package api.gabaritol.services.payment;

import java.util.UUID;
import api.gabaritol.entities.user.User;

public interface PaymentService {
    String createCheckout(User user, UUID creditPackageId);
    void handleWebhookConfirmation(String billId);
}