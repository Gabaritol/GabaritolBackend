package api.gabaritol.ai.gemini;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeneratedQuestionDTO(
    String statement,
    List<GeneratedOptionDTO> options,
    String explanation
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GeneratedOptionDTO(String label, String text, boolean correct) {}
}