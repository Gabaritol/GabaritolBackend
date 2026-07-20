package api.gabaritol.controllers.question;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.gabaritol.DTOs.question.*;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.question.ExamQuestion;
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

        List<QuestionResponseDTO> result = questionService.findExamQuestionsByExam(exam).stream()
            .map(this::toResponseDTO)
            .toList();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<QuestionWithAnswerResponseDTO>> listQuestionsWithAnswers(UUID examId, User currentUser) {
        Exam exam = examService.findByIdAndUser(examId, currentUser);

        List<QuestionWithAnswerResponseDTO> result = questionService.findExamQuestionsByExam(exam).stream()
            .map(this::toResponseWithAnswerDTO)
            .toList();

        return ResponseEntity.ok(result);
    }

    private QuestionResponseDTO toResponseDTO(ExamQuestion examQuestion) {
        var question = examQuestion.getQuestion();
        var options = question.getOptions().stream()
            .map(AnswerOptionResponseDTO::fromEntity)
            .toList();
        return QuestionResponseDTO.fromEntity(question, options, examQuestion.getOrderInExam());
    }

    private QuestionWithAnswerResponseDTO toResponseWithAnswerDTO(ExamQuestion examQuestion) {
        var question = examQuestion.getQuestion();
        var options = question.getOptions().stream()
            .map(AnswerOptionWithAnswerResponseDTO::fromEntity)
            .toList();
        return QuestionWithAnswerResponseDTO.fromEntity(question, options, examQuestion.getOrderInExam());
    }
}