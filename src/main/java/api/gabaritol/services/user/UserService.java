package api.gabaritol.services.user;

import java.util.UUID;

import api.gabaritol.entities.user.PlanType;
import api.gabaritol.entities.user.User;

public interface UserService {
    User register(String email);
    void requestLoginCode(String email);
    User verifyCode(String email, String submittedCode);
    User loginWithPassword(String email, String rawPassword);
    User upgradePlan(UUID userId, PlanType newPlan, String rawPassword);
}
