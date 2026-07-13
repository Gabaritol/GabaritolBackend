package api.gabaritol.services.notification;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendVerificationCode(String email, String code) {
        System.out.println("Sending verification code to " + email + ": " + code);
        // Implement the logic to send the verification code via email
    }
}
