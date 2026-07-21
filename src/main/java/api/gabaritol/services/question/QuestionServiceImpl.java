package api.gabaritol.services.question;

import java.util.List;
import org.springframework.stereotype.Service;

import api.gabaritol.DTOs.question.AnswerOptionResponseDTO;
import api.gabaritol.DTOs.question.AnswerOptionWithAnswerResponseDTO;
import api.gabaritol.DTOs.question.QuestionResponseDTO;
import api.gabaritol.DTOs.question.QuestionWithAnswerResponseDTO;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.question.ExamQuestion;
import api.gabaritol.repositories.question.ExamQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final ExamQuestionRepository examQuestionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<QuestionWithAnswerResponseDTO> findQuestionsWithAnswersByExam(Exam exam) {
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamOrderByOrderInExamAsc(exam);

        return examQuestions.stream()
            .map(eq -> {
                var question = eq.getQuestion();
                var options = question.getOptions().stream()
                    .map(AnswerOptionWithAnswerResponseDTO::fromEntity)
                    .toList();
                return QuestionWithAnswerResponseDTO.fromEntity(question, options, eq.getOrderInExam());
            })
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponseDTO> findQuestionsByExam(Exam exam) {
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamOrderByOrderInExamAsc(exam);

        return examQuestions.stream()
            .map(eq -> {
                var question = eq.getQuestion();
                var options = question.getOptions().stream()
                    .map(AnswerOptionResponseDTO::fromEntity)
                    .toList();
                return QuestionResponseDTO.fromEntity(question, options, eq.getOrderInExam());
            })
            .toList();
    }
}