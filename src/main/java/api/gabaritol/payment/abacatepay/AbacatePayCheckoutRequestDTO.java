package api.gabaritol.payment.abacatepay;

import java.util.List;

public record AbacatePayCheckoutRequestDTO(
    List<Item> items,
    String externalId,
    String returnUrl,
    String completionUrl
) {
    public record Item(String id, int quantity) {}
}