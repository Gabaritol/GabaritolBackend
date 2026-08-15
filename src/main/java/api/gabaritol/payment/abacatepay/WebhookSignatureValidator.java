package api.gabaritol.payment.abacatepay;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebhookSignatureValidator {

    @Value("${abacatepay.webhook.hmac-key}")
    private String hmacKey;

    public boolean isValid(String rawBody, String signatureFromHeader) {
        if (signatureFromHeader == null) return false;

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(hmacKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getEncoder().encodeToString(hash);

            byte[] expected = expectedSignature.getBytes(StandardCharsets.UTF_8);
            byte[] received = signatureFromHeader.getBytes(StandardCharsets.UTF_8);

            return MessageDigest.isEqual(expected, received);

        } catch (Exception e) {
            log.error("Error validating webhook signature", e);
            return false;
        }
    }
}