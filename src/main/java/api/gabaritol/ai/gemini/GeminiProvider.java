package api.gabaritol.ai.gemini;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import api.gabaritol.ai.AIProvider;
import api.gabaritol.exceptions.raises.AIProviderException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GeminiProvider implements AIProvider {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public String generateContent(String prompt) {
        log.info("Sending prompt to Gemini ({} chars)", prompt.length());
        try {
            GeminiRequestDTO request = GeminiRequestDTO.of(prompt);

            GeminiResponseDTO response = webClient.post()
                .uri(apiUrl)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponseDTO.class)
                .block();

            if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
                throw new AIProviderException("Gemini returned an empty response.");
            }

            String text = response.extractText();
            log.info("Gemini responded successfully ({} chars)", text.length());
            return text;

        } catch (Exception e) {
            log.error("Error calling Gemini API", e);
            throw new AIProviderException("Failed to generate content with Gemini.");
        }
    }
}