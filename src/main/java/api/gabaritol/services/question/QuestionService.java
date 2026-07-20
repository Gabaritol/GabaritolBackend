package api.gabaritol.services.question;

import java.util.List;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.question.ExamQuestion;

public interface QuestionService {
    List<ExamQuestion> findExamQuestionsByExam(Exam exam);
}