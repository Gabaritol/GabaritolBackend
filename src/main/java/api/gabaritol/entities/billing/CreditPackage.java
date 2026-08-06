package api.gabaritol.entities.billing;

import java.util.UUID;
import api.gabaritol.entities.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CreditPackage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private Integer creditAmount;
    private Integer priceInCents;
    private String abacatePayProductId;
    private Boolean active;
}