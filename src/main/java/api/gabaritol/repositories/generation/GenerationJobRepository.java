package api.gabaritol.repositories.generation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.generation.GenerationJob;
import api.gabaritol.entities.generation.JobStatus;

public interface GenerationJobRepository extends JpaRepository<GenerationJob, UUID> {
    List<GenerationJob> findByExam(Exam exam);
    List<GenerationJob> findByStatus(JobStatus status);
    List<GenerationJob> findByStatusAndStartedAtBefore(JobStatus status, LocalDateTime cutoff);
    List<GenerationJob> findByStatusInAndFinishedAtBefore(List<JobStatus> statuses, LocalDateTime cutoff);
}