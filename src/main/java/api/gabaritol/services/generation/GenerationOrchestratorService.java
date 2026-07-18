package api.gabaritol.services.generation;

import java.util.UUID;

public interface GenerationOrchestratorService {
    void generateQuestionsForExam(UUID examId);
}