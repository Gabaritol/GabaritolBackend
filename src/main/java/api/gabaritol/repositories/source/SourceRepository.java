package api.gabaritol.repositories.source;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.source.Source;

public interface SourceRepository extends JpaRepository<Source, UUID> {
    List<Source> findByExam(Exam exam);
}