package api.gabaritol.entities.billing;

import java.util.UUID;

import api.gabaritol.entities.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "credit_cost_per_model")
public class CreditCostPerModel extends BaseEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String modelName;

    @Enumerated(EnumType.STRING)
    private AIRole role;

    private Integer creditCostPerQuestion;

    private Boolean active;
}