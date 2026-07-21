package api.gabaritol.controllers.question;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.question.*;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;
import api.gabaritol.services.exam.ExamService;
import api.gabaritol.services.question.QuestionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class QuestionControllerImpl implements QuestionController {

    private final QuestionService questionService;
    private final ExamService examService;

    @Override
    public ResponseEntity<List<QuestionResponseDTO>> listQuestions(UUID examId, User currentUser) {
        Exam exam = examService.findByIdAndUser(examId, currentUser);
        return ResponseEntity.ok(questionService.findQuestionsByExam(exam));
    }

    @Override
    public ResponseEntity<List<QuestionWithAnswerResponseDTO>> listQuestionsWithAnswers(UUID examId, User currentUser) {
        Exam exam = examService.findByIdAndUser(examId, currentUser);
        return ResponseEntity.ok(questionService.findQuestionsWithAnswersByExam(exam));
    }
}