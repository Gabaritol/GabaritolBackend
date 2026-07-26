package api.gabaritol.DTOs.billing;

import java.time.LocalDateTime;
import java.util.UUID;
import api.gabaritol.entities.billing.Transaction;
import api.gabaritol.entities.billing.TransactionType;

public record TransactionResponseDTO(
    UUID id,
    TransactionType type,
    Integer creditAmount,
    UUID relatedExamId,
    LocalDateTime createdAt
) {
    public static TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
            transaction.getId(),
            transaction.getType(),
            transaction.getCreditAmount(),
            transaction.getRelatedExam() != null ? transaction.getRelatedExam().getId() : null,
            transaction.getCreatedAt()
        );
    }
}