package api.gabaritol.services.user;

import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import api.gabaritol.entities.user.PlanType;
import api.gabaritol.entities.user.Role;
import api.gabaritol.entities.user.User;
import api.gabaritol.repositories.user.UserRepository;
import api.gabaritol.services.notification.EmailService;
import api.gabaritol.util.TokenGenerator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final int CODE_EXPIRATION_MINUTES = 15;
    private static final int MAX_VERIFICATION_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator generator;
    private final EmailService emailService;

    public User register(String email) {
        User user = userRepository.findByEmail(email).orElseGet(User::new);

        if (Boolean.TRUE.equals(user.isVerified())) {
            throw new IllegalStateException("Email already registered and verified.");
        }

        user.setEmail(email);
        user.setUsername(email.substring(0, email.indexOf("@")));
        user.setRole(Role.USER);
        user.setPlan(PlanType.FREE);
        user.setAvailableCredits(0);

        user.setVerificationCode(generator.generateToken());

        User saved = userRepository.save(user);
        emailService.sendVerificationCode(saved.getEmail(), saved.getVerificationCode());
        return saved;
    }

    public User verifyCode(String email, String submittedCode) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (user.isVerificationCodeExpired()) {
            throw new IllegalStateException("Verification code expired. Request a new one.");
        }

        if (user.getVerificationAttempts() >= MAX_VERIFICATION_ATTEMPTS) {
            throw new IllegalStateException("Too many attempts. Request a new code.");
        }

        if (!user.getVerificationCode().equals(submittedCode)) {
            user.increaseVerificationAttempts();
            userRepository.save(user);
            throw new IllegalArgumentException("Invalid verification code.");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setCodeExpiresAt(null);
        return userRepository.save(user);
    }

    public void requestLoginCode(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setVerificationCode(generator.generateToken());
        userRepository.save(user);
        emailService.sendVerificationCode(user.getEmail(), user.getVerificationCode());
    }

    public User loginWithPassword(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (user.getPassword() == null) {
            throw new IllegalStateException("This account has no password set.");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        return user;
    }

    public User upgradePlan(UUID userId, PlanType newPlan, String rawPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (newPlan != PlanType.FREE && (rawPassword == null || rawPassword.isBlank())) {
            throw new IllegalArgumentException("Password is required to upgrade to a paid plan.");
        }

        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        user.setPlan(newPlan);
        return userRepository.save(user);
    }
}