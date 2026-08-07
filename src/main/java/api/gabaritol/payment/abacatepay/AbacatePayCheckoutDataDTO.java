package api.gabaritol.payment.abacatepay;

public record AbacatePayCheckoutDataDTO(
    String id,
    String url,
    Integer amount,
    String status
) {}