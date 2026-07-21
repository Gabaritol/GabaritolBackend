package api.gabaritol.services.question;

import java.util.List;

import api.gabaritol.DTOs.question.QuestionResponseDTO;
import api.gabaritol.DTOs.question.QuestionWithAnswerResponseDTO;
import api.gabaritol.entities.exam.Exam;

public interface QuestionService {
    List<QuestionWithAnswerResponseDTO> findQuestionsWithAnswersByExam(Exam exam);
    List<QuestionResponseDTO> findQuestionsByExam(Exam exam);
}