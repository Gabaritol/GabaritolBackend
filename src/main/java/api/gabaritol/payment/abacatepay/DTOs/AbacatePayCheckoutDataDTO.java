package api.gabaritol.payment.abacatepay.DTOs;

public record AbacatePayCheckoutDataDTO(
    String id,
    String url,
    Integer amount,
    String status
) {}