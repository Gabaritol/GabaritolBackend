package api.gabaritol.scheduled;

import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import api.gabaritol.entities.user.*;
import api.gabaritol.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditRechargeService {

    private static final int BASE_CREDIT_CAP = 25;
    private static final int RECHARGE_AMOUNT = 5;

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void rechargeFreeUserCredits() {
        List<User> freeUsers = userRepository.findByPlan(PlanType.FREE);
        int rechargedCount = 0;

        for (User user : freeUsers) {
            int bonusCap = user.getReferralBonusCap() != null ? user.getReferralBonusCap() : 0;
            int userCap = BASE_CREDIT_CAP + bonusCap;

            int currentCredits = user.getAvailableCredits() != null ? user.getAvailableCredits() : 0;

            if (currentCredits < userCap) {
                int newBalance = Math.min(currentCredits + RECHARGE_AMOUNT, userCap);
                user.setAvailableCredits(newBalance);
                userRepository.save(user);
                rechargedCount++;
            }
        }

        if (rechargedCount > 0) {
            log.info("Recharged credits for {} FREE users.", rechargedCount);
        }
    }
}