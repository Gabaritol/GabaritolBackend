package api.gabaritol.repositories.exam;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;

public interface ExamRepository extends JpaRepository<Exam, UUID> {
    List<Exam> findByUser(User user);
    List<Exam> findByUserOrderByCreatedAtDesc(User user);
}
