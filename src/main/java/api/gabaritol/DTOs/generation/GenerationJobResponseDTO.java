package api.gabaritol.DTOs.generation;

import java.util.UUID;
import api.gabaritol.entities.generation.GenerationJob;
import api.gabaritol.entities.generation.JobStatus;

public record GenerationJobResponseDTO(
    UUID id,
    UUID examId,
    JobStatus status,
    Integer questionsGenerated,
    Integer questionsTotal,
    String errorMessage
) {
    public static GenerationJobResponseDTO fromEntity(GenerationJob job) {
        return new GenerationJobResponseDTO(
            job.getId(),
            job.getExam().getId(),
            job.getStatus(),
            job.getQuestionsGenerated(),
            job.getQuestionsTotal(),
            job.getErrorMessage()
        );
    }
}