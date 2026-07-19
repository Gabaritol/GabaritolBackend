package api.gabaritol.controllers.generation;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import api.gabaritol.DTOs.generation.GenerationJobResponseDTO;
import api.gabaritol.DTOs.generation.StartGenerationResponseDTO;
import api.gabaritol.entities.user.User;

public interface GenerationController {

    @PostMapping("/api/exams/{examId}/generate")
    ResponseEntity<StartGenerationResponseDTO> generate(
        @PathVariable UUID examId,
        @AuthenticationPrincipal User currentUser
    );

    @GetMapping("/api/generation-jobs/{jobId}")
    ResponseEntity<GenerationJobResponseDTO> getJobStatus(
        @PathVariable UUID jobId,
        @AuthenticationPrincipal User currentUser
    );
}