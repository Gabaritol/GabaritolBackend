package api.gabaritol.controllers.test;

import org.springframework.web.bind.annotation.*;
import api.gabaritol.ai.AIProvider;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestAIController {

    private final AIProvider aiProvider;

    @GetMapping("/ai")
    public String testAI(@RequestParam(defaultValue = "Explique o que é uma questão de múltipla escolha em uma frase.") String prompt) {
        return aiProvider.generateContent(prompt);
    }
}