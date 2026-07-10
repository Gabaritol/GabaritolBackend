package api.gabaritol.repositories.billing;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.billing.AIRole;
import api.gabaritol.entities.billing.CreditCostPerModel;

public interface CreditCostPerModelRepository extends JpaRepository<CreditCostPerModel, UUID> {
    Optional<CreditCostPerModel> findByModelNameAndRoleAndActiveTrue(String modelName, AIRole role);
}