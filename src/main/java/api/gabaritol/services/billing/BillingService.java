package api.gabaritol.services.billing;

import api.gabaritol.entities.billing.AIRole;
import api.gabaritol.entities.billing.Transaction;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;

public interface BillingService {
    int calculateCost(String modelName, AIRole role, int questionCount);
    boolean hasSufficientCredits(User user, int cost);
    Transaction debitCredits(User user, int amount, Exam exam);
}