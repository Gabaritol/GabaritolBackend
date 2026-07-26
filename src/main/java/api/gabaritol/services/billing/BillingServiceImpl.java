package api.gabaritol.services.billing;

import java.util.List;

import org.springframework.stereotype.Service;

import api.gabaritol.DTOs.user.UserCreditsResponseDTO;
import api.gabaritol.entities.billing.*;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.repositories.billing.CreditCostPerModelRepository;
import api.gabaritol.repositories.billing.TransactionRepository;
import api.gabaritol.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingServiceImpl implements BillingService {

    private final CreditCostPerModelRepository creditCostPerModelRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private static final int BASE_CREDIT_CAP = 25;

    @Override
    public int calculateCost(String modelName, AIRole role, int questionCount) {
        CreditCostPerModel cost = creditCostPerModelRepository
            .findByModelNameAndRoleAndActiveTrue(modelName, role)
            .orElseThrow(() -> new NotFoundException(
                "No active credit cost configured for model " + modelName + " / role " + role));

        return cost.getCreditCostPerQuestion() * questionCount;
    }

    @Override
    public boolean hasSufficientCredits(User user, int cost) {
        return user.getAvailableCredits() != null && user.getAvailableCredits() >= cost;
    }

    @Override
    public Transaction debitCredits(User user, int amount, Exam exam) {
        user.setAvailableCredits(user.getAvailableCredits() - amount);
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(TransactionType.GENERATION_CONSUMPTION);
        transaction.setCreditAmount(-amount);
        transaction.setRelatedExam(exam);

        log.info("Debited {} credits from user {} for exam {}. Remaining balance: {}",
            amount, user.getId(), exam.getId(), user.getAvailableCredits());

        return transactionRepository.save(transaction);
    }

    @Override
    public UserCreditsResponseDTO getUserCredits(User user) {
        int bonusCap = user.getReferralBonusCap() != null ? user.getReferralBonusCap() : 0;
        int maxCap = BASE_CREDIT_CAP + bonusCap;
        return new UserCreditsResponseDTO(user.getAvailableCredits(), maxCap);
    }

    @Override
    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findByUserOrderByCreatedAtDesc(user);
    }
}