package api.gabaritol.services.generation;

import java.util.UUID;

public interface GenerationOrchestratorService {
    UUID startGeneration(UUID examId);
}