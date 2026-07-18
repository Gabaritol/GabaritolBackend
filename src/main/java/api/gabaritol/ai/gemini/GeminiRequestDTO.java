package api.gabaritol.ai.gemini;

import java.util.List;

public record GeminiRequestDTO(List<Content> contents) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    public static GeminiRequestDTO of(String prompt) {
        return new GeminiRequestDTO(List.of(
            new Content(List.of(new Part(prompt)))
        ));
    }
}