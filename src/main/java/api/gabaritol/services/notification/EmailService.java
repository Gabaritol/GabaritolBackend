package api.gabaritol.services.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;
     
    public void sendVerificationCode(String email, String verificationCode) {
        log.info("Sending verification email to {}", email);
        try {
            Context context = new Context();
            context.setVariable("verificationCode", verificationCode);

            String htmlContent = templateEngine.process("verification-email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Verifique seu email - Gabaritol");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Verification email sent to {}", email);
        } catch (MailException | MessagingException e) {
            log.error("Failed to send verification email to {}", email, e);
            throw new RuntimeException("Failed to send verification email", e);
        }  
    }
}
