package api.gabaritol.services.generation;

import java.util.UUID;
import org.springframework.stereotype.Service;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.generation.GenerationJob;
import api.gabaritol.entities.generation.JobStatus;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.repositories.exam.ExamRepository;
import api.gabaritol.repositories.generation.GenerationJobRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenerationOrchestratorServiceImpl implements GenerationOrchestratorService {

    private final ExamRepository examRepository;
    private final GenerationJobRepository generationJobRepository;
    private final AsyncGenerationWorker asyncGenerationWorker;

    @Override
    public UUID startGeneration(UUID examId) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new NotFoundException("Exam not found."));

        GenerationJob job = new GenerationJob();
        job.setExam(exam);
        job.setStatus(JobStatus.PENDING);
        job.setQuestionsTotal(exam.getQuestionCount());
        job.setQuestionsGenerated(0);
        GenerationJob savedJob = generationJobRepository.save(job);

        asyncGenerationWorker.process(savedJob.getId(), examId);

        return savedJob.getId();
    }

    @Override
    public GenerationJob findJobById(UUID jobId) {
        return generationJobRepository.findById(jobId)
            .orElseThrow(() -> new NotFoundException("Generation job not found."));
    }
}