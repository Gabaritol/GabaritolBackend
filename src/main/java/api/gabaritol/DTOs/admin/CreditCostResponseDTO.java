package api.gabaritol.DTOs.admin;

import java.util.UUID;
import api.gabaritol.entities.billing.AIRole;
import api.gabaritol.entities.billing.CreditCostPerModel;

public record CreditCostResponseDTO(
    UUID id, String modelName, AIRole role, Integer creditCostPerQuestion, Boolean active
) {
    public static CreditCostResponseDTO fromEntity(CreditCostPerModel cost) {
        return new CreditCostResponseDTO(
            cost.getId(), cost.getModelName(), cost.getRole(),
            cost.getCreditCostPerQuestion(), cost.getActive()
        );
    }
}