package api.gabaritol.services.question;

import java.util.List;
import org.springframework.stereotype.Service;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.question.ExamQuestion;
import api.gabaritol.repositories.question.ExamQuestionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final ExamQuestionRepository examQuestionRepository;

    @Override
    public List<ExamQuestion> findExamQuestionsByExam(Exam exam) {
        return examQuestionRepository.findByExamOrderByOrderInExamAsc(exam);
    }
}