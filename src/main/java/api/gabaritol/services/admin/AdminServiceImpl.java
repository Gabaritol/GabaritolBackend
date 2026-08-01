package api.gabaritol.services.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import api.gabaritol.entities.billing.AIRole;
import api.gabaritol.entities.billing.CreditCostPerModel;
import api.gabaritol.entities.user.User;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.repositories.billing.CreditCostPerModelRepository;
import api.gabaritol.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final CreditCostPerModelRepository creditCostPerModelRepository;
    private final UserRepository userRepository;

    @Override
    public CreditCostPerModel createCreditCost(String modelName, AIRole role, int costPerQuestion) {
        CreditCostPerModel cost = new CreditCostPerModel();
        cost.setModelName(modelName);
        cost.setRole(role);
        cost.setCreditCostPerQuestion(costPerQuestion);
        cost.setActive(true);
        return creditCostPerModelRepository.save(cost);
    }

    @Override
    public List<CreditCostPerModel> listCreditCosts() {
        return creditCostPerModelRepository.findAll();
    }

    @Override
    public void deactivateCreditCost(UUID id) {
        CreditCostPerModel cost = creditCostPerModelRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Credit cost not found."));
        cost.setActive(false);
        creditCostPerModelRepository.save(cost);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User adjustUserCredits(UUID userId, int amount) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found."));

        int current = user.getAvailableCredits() != null ? user.getAvailableCredits() : 0;
        user.setAvailableCredits(Math.max(0, current + amount));

        log.info("Admin adjusted user {} credits by {}. New balance: {}", userId, amount, user.getAvailableCredits());
        return userRepository.save(user);
    }

    @Override
    public void banUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found."));
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        log.warn("Admin banned user {}", userId);
    }
}