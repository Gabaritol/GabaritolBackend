package api.gabaritol.services.user;

import java.time.LocalDateTime;
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
public class UserServiceImpl implements UserService {

    private static final int CODE_EXPIRATION_MINUTES = 15;
    private static final int MAX_VERIFICATION_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator generator;
    private final EmailService emailService;

    private void issueVerificationCode(User user) {
        user.setVerificationCode(generator.generateToken());
        user.setCodeExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));
        user.setVerificationAttempts(0);
    }

    @Override
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

        issueVerificationCode(user);

        User saved = userRepository.save(user);
        emailService.sendVerificationCode(saved.getEmail(), saved.getVerificationCode());
        return saved;
    }

    @Override
    public void requestLoginCode(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found."));

        issueVerificationCode(user);
        userRepository.save(user);
        emailService.sendVerificationCode(user.getEmail(), user.getVerificationCode());
    }

    @Override
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

        if (!user.isVerified()) {
            user.setVerified(true);
        }

        user.setVerificationCode(null);
        user.setCodeExpiresAt(null);
        user.setVerificationAttempts(0);
        return userRepository.save(user);
    }

    @Override
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
    
    @Override
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