package api.gabaritol.services.exam;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import api.gabaritol.entities.exam.Difficulty;
import api.gabaritol.entities.exam.EducationLevel;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.exam.ExamStatus;
import api.gabaritol.entities.user.User;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.repositories.exam.ExamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    @Override
    public Exam createDraft(
        User user, 
        String title, 
        String board, 
        String topic,
        String position, 
        Difficulty difficulty,
        EducationLevel educationLevel,
        Integer questionCount
    ) {
        Exam exam = new Exam();
        exam.setUser(user);
        exam.setTitle(title);
        exam.setBoard(board != null ? normalize(board) : null);
        exam.setTopic(normalize(topic));
        exam.setPosition(position);
        exam.setDifficulty(difficulty);
        exam.setEducationLevel(educationLevel);
        exam.setQuestionCount(questionCount);
        exam.setStatus(ExamStatus.DRAFT);
        return examRepository.save(exam);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }

    @Override
    public List<Exam> findByUser(User user) {
        return examRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Exam findById(UUID id) {
        return examRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Exam not found."));
    }

    @Override
    public Exam findByIdAndUser(UUID id, User user) {
        Exam exam = findById(id);
        if (!exam.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Exam not found.");
        }
        return exam;
    }
}