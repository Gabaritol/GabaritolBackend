package api.gabaritol.payment.abacatepay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import api.gabaritol.exceptions.raises.PaymentException;
import api.gabaritol.payment.abacatepay.DTOs.AbacatePayCheckoutDataDTO;
import api.gabaritol.payment.abacatepay.DTOs.AbacatePayCheckoutRequestDTO;
import api.gabaritol.payment.abacatepay.DTOs.AbacatePayEnvelopeDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AbacatePayClient {

    private final WebClient webClient;

    @Value("${abacatepay.api.key}")
    private String apiKey;

    public AbacatePayClient(WebClient.Builder builder, @Value("${abacatepay.api.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public AbacatePayCheckoutDataDTO createCheckout(String productId, int quantity, String externalId) {
        try {
            AbacatePayCheckoutRequestDTO request = new AbacatePayCheckoutRequestDTO(
                java.util.List.of(new AbacatePayCheckoutRequestDTO.Item(productId, quantity)),
                externalId,
                "https://gabaritol.com.br/pagamento/cancelado",
                "https://gabaritol.com.br/pagamento/sucesso"
            );

            AbacatePayEnvelopeDTO<AbacatePayCheckoutDataDTO> response = webClient.post()
                .uri("/checkouts/create")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AbacatePayEnvelopeDTO<AbacatePayCheckoutDataDTO>>() {})
                .block();

            if (response == null || Boolean.FALSE.equals(response.success())) {
                throw new PaymentException("AbacatePay checkout creation failed.");
            }

            return response.data();

        } catch (Exception e) {
            log.error("Error creating AbacatePay checkout", e);
            throw new PaymentException("Failed to create checkout.");
        }
    }
}