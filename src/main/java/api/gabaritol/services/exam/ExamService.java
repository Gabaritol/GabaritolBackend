package api.gabaritol.services.exam;

import java.util.List;
import java.util.UUID;
import api.gabaritol.entities.exam.Difficulty;
import api.gabaritol.entities.exam.EducationLevel;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;

public interface ExamService {
    Exam createDraft(
        User user, 
        String title, 
        String board, 
        String topic,
        String position, 
        Difficulty difficulty, 
        EducationLevel educationLevel,
        Integer questionCount
    );
    List<Exam> findByUser(User user);
    Exam findById(UUID id);
    Exam findByIdAndUser(UUID id, User user);
}