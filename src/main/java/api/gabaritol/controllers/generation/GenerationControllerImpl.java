package api.gabaritol.controllers.generation;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.generation.GenerationJobResponseDTO;
import api.gabaritol.DTOs.generation.StartGenerationResponseDTO;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.generation.GenerationJob;
import api.gabaritol.entities.user.User;
import api.gabaritol.services.exam.ExamService;
import api.gabaritol.services.generation.GenerationOrchestratorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GenerationControllerImpl implements GenerationController {

    private final GenerationOrchestratorService generationOrchestratorService;
    private final ExamService examService;

    @Override
    public ResponseEntity<StartGenerationResponseDTO> generate(UUID examId, User currentUser) {
        Exam exam = examService.findByIdAndUser(examId, currentUser);
        UUID jobId = generationOrchestratorService.startGeneration(exam.getId());
        return ResponseEntity.ok(new StartGenerationResponseDTO(jobId));
    }

    @Override
    public ResponseEntity<GenerationJobResponseDTO> getJobStatus(UUID jobId, User currentUser) {
        GenerationJob job = generationOrchestratorService.findJobById(jobId);
        if (!job.getExam().getUser().getId().equals(currentUser.getId())) {
            throw new api.gabaritol.exceptions.raises.NotFoundException("Generation job not found.");
        }
        return ResponseEntity.ok(GenerationJobResponseDTO.fromEntity(job));
    }
}