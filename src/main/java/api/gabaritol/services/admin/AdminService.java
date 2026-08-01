package api.gabaritol.services.admin;

import java.util.List;
import java.util.UUID;
import api.gabaritol.entities.billing.AIRole;
import api.gabaritol.entities.billing.CreditCostPerModel;
import api.gabaritol.entities.user.User;

public interface AdminService {
    CreditCostPerModel createCreditCost(
        String modelName, 
        AIRole role, 
        int costPerQuestion
    );
    List<CreditCostPerModel> listCreditCosts();
    void deactivateCreditCost(UUID id);

    List<User> listUsers();
    User adjustUserCredits(UUID userId, int amount);
    void banUser(UUID userId);
}