package api.gabaritol.repositories.question;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.question.ExamQuestion;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, UUID> {
    List<ExamQuestion> findByExamOrderByOrderInExamAsc(Exam exam);
}