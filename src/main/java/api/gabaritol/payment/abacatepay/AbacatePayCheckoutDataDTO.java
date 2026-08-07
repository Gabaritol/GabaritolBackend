package api.gabaritol.payment.abacatepay;

public record AbacatePayCheckoutDataDTO(
    String id,       // "bill_abc123"
    String url,       // URL do checkout
    Integer amount,
    String status
) {}