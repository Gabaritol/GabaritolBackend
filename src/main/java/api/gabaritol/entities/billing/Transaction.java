package api.gabaritol.entities.billing;

import java.math.BigDecimal;
import java.util.UUID;
import api.gabaritol.entities.common.BaseEntity;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Integer creditAmount;
    private BigDecimal amountPaid;

    @ManyToOne
    private Exam relatedExam;
}