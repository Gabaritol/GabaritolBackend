package api.gabaritol.services.payment;

import org.springframework.stereotype.Service;
import api.gabaritol.entities.billing.CreditPackage;
import api.gabaritol.entities.billing.Transaction;
import api.gabaritol.entities.billing.TransactionType;
import api.gabaritol.entities.user.User;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.payment.abacatepay.AbacatePayClient;
import api.gabaritol.payment.abacatepay.DTOs.AbacatePayCheckoutDataDTO;
import api.gabaritol.repositories.billing.CreditPackageRepository;
import api.gabaritol.repositories.billing.TransactionRepository;
import api.gabaritol.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final AbacatePayClient abacatePayClient;
    private final CreditPackageRepository creditPackageRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public String createCheckout(User user, UUID creditPackageId) {
        CreditPackage pkg = creditPackageRepository.findById(creditPackageId)
            .orElseThrow(() -> new NotFoundException("Credit package not found."));

        String externalId = "user_" + user.getId() + "_pkg_" + pkg.getId();

        AbacatePayCheckoutDataDTO checkout = abacatePayClient.createCheckout(
            pkg.getAbacatePayProductId(), 1, externalId
        );

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(TransactionType.CREDIT_PURCHASE);
        transaction.setCreditAmount(pkg.getCreditAmount());
        transaction.setExternalPaymentId(checkout.id());
        transaction.setPaymentStatus("PENDING");
        transactionRepository.save(transaction);

        return checkout.url();
    }

    @Override
    public void handleWebhookConfirmation(String billId) {
        Transaction transaction = transactionRepository.findByExternalPaymentId(billId)
            .orElseThrow(() -> new NotFoundException("Transaction not found for bill " + billId));

        if ("PAID".equals(transaction.getPaymentStatus())) {
            log.info("Bill {} already processed, ignoring duplicate webhook.", billId);
            return;
        }

        transaction.setPaymentStatus("PAID");
        transactionRepository.save(transaction);

        User user = transaction.getUser();
        int current = user.getAvailableCredits() != null ? user.getAvailableCredits() : 0;
        user.setAvailableCredits(current + transaction.getCreditAmount());
        userRepository.save(user);

        log.info("Credited {} credits to user {} via payment {}", transaction.getCreditAmount(), user.getId(), billId);
    }
}