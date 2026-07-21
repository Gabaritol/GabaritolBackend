package api.gabaritol.controllers.exam;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import api.gabaritol.DTOs.exam.*;
import api.gabaritol.entities.user.User;
import jakarta.validation.Valid;

@RequestMapping("/api/exams")
public interface ExamController {

    @PostMapping
    ResponseEntity<ExamResponseDTO> create(
        @Valid @RequestBody CreateExamRequestDTO request,
        @AuthenticationPrincipal User currentUser
    );

    @GetMapping
    ResponseEntity<List<ExamResponseDTO>> listMine(@AuthenticationPrincipal User currentUser);

    @GetMapping("/{id}")
    ResponseEntity<ExamResponseDTO> getOne(
        @PathVariable UUID id,
        @AuthenticationPrincipal User currentUser
    );
}