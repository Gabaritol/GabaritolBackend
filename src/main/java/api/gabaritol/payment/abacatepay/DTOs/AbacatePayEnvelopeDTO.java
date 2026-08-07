package api.gabaritol.payment.abacatepay;

public record AbacatePayEnvelopeDTO<T>(
    T data,
    Boolean success,
    Object error
) {}