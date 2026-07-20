package api.gabaritol.controllers.question;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import api.gabaritol.DTOs.question.QuestionResponseDTO;
import api.gabaritol.DTOs.question.QuestionWithAnswerResponseDTO;
import api.gabaritol.entities.user.User;

@RequestMapping("/api/exams/{examId}/questions")
public interface QuestionController {

    @GetMapping
    ResponseEntity<List<QuestionResponseDTO>> listQuestions(
        @PathVariable UUID examId,
        @AuthenticationPrincipal User currentUser
    );

    @GetMapping("/answers")
    ResponseEntity<List<QuestionWithAnswerResponseDTO>> listQuestionsWithAnswers(
        @PathVariable UUID examId,
        @AuthenticationPrincipal User currentUser
    );
}