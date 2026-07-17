package api.gabaritol.controllers.source;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import api.gabaritol.DTOs.source.PastedTextRequestDTO;
import api.gabaritol.DTOs.source.SourceResponseDTO;
import api.gabaritol.entities.user.User;
import jakarta.validation.Valid;

@RequestMapping("/api/exams/{examId}/sources")
public interface SourceController {

    @PostMapping(value = "/pdf", consumes = "multipart/form-data")
    ResponseEntity<SourceResponseDTO> uploadPdf(
        @PathVariable UUID examId,
        @RequestParam("file") MultipartFile file,
        @AuthenticationPrincipal User currentUser
    );

    @PostMapping("/text")
    ResponseEntity<SourceResponseDTO> addText(
        @PathVariable UUID examId,
        @Valid @RequestBody PastedTextRequestDTO request,
        @AuthenticationPrincipal User currentUser
    );

    @GetMapping
    ResponseEntity<List<SourceResponseDTO>> listByExam(
        @PathVariable UUID examId,
        @AuthenticationPrincipal User currentUser
    );
}