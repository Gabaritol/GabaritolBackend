package api.gabaritol.payment.abacatepay.DTOs;

public record AbacatePayEnvelopeDTO<T>(
    T data,
    Boolean success,
    Object error
) {}