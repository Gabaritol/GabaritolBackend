package api.gabaritol.services.generation;

import java.util.UUID;

import api.gabaritol.entities.generation.GenerationJob;

public interface GenerationOrchestratorService {
    UUID startGeneration(UUID examId);
    GenerationJob findJobById(UUID jobId);
}