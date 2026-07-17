package api.gabaritol.controllers.source;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import api.gabaritol.DTOs.source.PastedTextRequestDTO;
import api.gabaritol.DTOs.source.SourceResponseDTO;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.source.Source;
import api.gabaritol.entities.user.User;
import api.gabaritol.services.exam.ExamService;
import api.gabaritol.services.source.SourceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SourceControllerImpl implements SourceController {

    private final SourceService sourceService;
    private final ExamService examService;

    @Override
    public ResponseEntity<SourceResponseDTO> uploadPdf(UUID examId, MultipartFile file, User currentUser) {
        Exam exam = examService.findByIdAndUser(examId, currentUser);
        Source source = sourceService.uploadPdf(exam, file);
        return ResponseEntity.ok(SourceResponseDTO.fromEntity(source));
    }

    @Override
    public ResponseEntity<SourceResponseDTO> addText(UUID examId, PastedTextRequestDTO request, User currentUser) {
        Exam exam = examService.findByIdAndUser(examId, currentUser);
        Source source = sourceService.addPastedText(exam, request.text());
        return ResponseEntity.ok(SourceResponseDTO.fromEntity(source));
    }

    @Override
    public ResponseEntity<List<SourceResponseDTO>> listByExam(UUID examId, User currentUser) {
        Exam exam = examService.findByIdAndUser(examId, currentUser);
        List<SourceResponseDTO> sources = sourceService.findByExam(exam).stream()
            .map(SourceResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(sources);
    }
}