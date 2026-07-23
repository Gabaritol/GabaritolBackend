package api.gabaritol.controllers.exam;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.exam.*;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;
import api.gabaritol.services.exam.ExamService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExamControllerImpl implements ExamController {

    private final ExamService examService;

    @Override
    public ResponseEntity<ExamResponseDTO> create(CreateExamRequestDTO request, User currentUser) {
        Exam exam = examService.createDraft(
            currentUser, 
            request.title(), 
            request.board(), 
            request.topic(),
            request.position(), 
            request.difficulty(),
            request.educationLevel(),
            request.questionCount()
        );
        return ResponseEntity.ok(ExamResponseDTO.fromEntity(exam));
    }

    @Override
    public ResponseEntity<List<ExamResponseDTO>> listMine(User currentUser) {
        List<ExamResponseDTO> exams = examService.findByUser(currentUser).stream()
            .map(ExamResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(exams);
    }

    @Override
    public ResponseEntity<ExamResponseDTO> getOne(UUID id, User currentUser) {
        Exam exam = examService.findByIdAndUser(id, currentUser);
        return ResponseEntity.ok(ExamResponseDTO.fromEntity(exam));
    }
}