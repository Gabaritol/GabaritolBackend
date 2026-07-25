package api.gabaritol.config.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import api.gabaritol.entities.billing.AIRole;
import api.gabaritol.entities.billing.CreditCostPerModel;
import api.gabaritol.repositories.billing.CreditCostPerModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreditCostSeeder implements CommandLineRunner {

    private final CreditCostPerModelRepository repository;

    @Override
    public void run(String... args) {
        seedIfNotExists("gemini-3.5-flash", AIRole.GENERATOR, 1);
    }

    private void seedIfNotExists(String modelName, AIRole role, int costPerQuestion) {
        boolean exists = repository.findByModelNameAndRoleAndActiveTrue(modelName, role).isPresent();

        if (!exists) {
            CreditCostPerModel cost = new CreditCostPerModel();
            cost.setModelName(modelName);
            cost.setRole(role);
            cost.setCreditCostPerQuestion(costPerQuestion);
            cost.setActive(true);
            repository.save(cost);
            log.info("Seeded credit cost: {} / {} = {} credits per question", modelName, role, costPerQuestion);
        }
    }
}