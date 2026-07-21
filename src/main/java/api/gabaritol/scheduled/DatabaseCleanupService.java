package api.gabaritol.scheduled;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import api.gabaritol.entities.exam.ExamStatus;
import api.gabaritol.entities.generation.GenerationJob;
import api.gabaritol.entities.generation.JobStatus;
import api.gabaritol.entities.user.User;
import api.gabaritol.repositories.generation.GenerationJobRepository;
import api.gabaritol.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseCleanupService {

    private final UserRepository userRepository;
    private final GenerationJobRepository generationJobRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupUnverifiedUsers() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        List<User> unverified = userRepository.findByVerifiedFalseAndCreatedAtBefore(cutoff);

        if (!unverified.isEmpty()) {
            log.info("Deleting {} unverified users older than 7 days.", unverified.size());
            userRepository.deleteAll(unverified);
        }
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void detectStuckGenerationJobs() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(10);
        List<GenerationJob> stuckJobs = generationJobRepository
            .findByStatusAndStartedAtBefore(JobStatus.GENERATING, cutoff);

        for (GenerationJob job : stuckJobs) {
            log.warn("Marking stuck generation job {} as FAILED (started at {}).",
                job.getId(), job.getStartedAt());
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage("Job timed out or the application restarted mid-process.");
            job.getExam().setStatus(ExamStatus.FAILED);
            generationJobRepository.save(job);
        }
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    public void cleanupOldFinishedJobs() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        List<GenerationJob> oldJobs = generationJobRepository
            .findByStatusInAndFinishedAtBefore(
                List.of(JobStatus.COMPLETED, JobStatus.FAILED), cutoff
            );

        if (!oldJobs.isEmpty()) {
            log.info("Deleting {} old finished generation jobs.", oldJobs.size());
            generationJobRepository.deleteAll(oldJobs);
        }
    }
}