package api.gabaritol.ai.gemini;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeneratedQuestionsBatchDTO(List<GeneratedQuestionDTO> questions) {}