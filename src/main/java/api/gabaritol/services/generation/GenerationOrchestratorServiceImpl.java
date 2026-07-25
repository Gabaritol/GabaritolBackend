package api.gabaritol.services.generation;

import java.util.UUID;
import org.springframework.stereotype.Service;

import api.gabaritol.entities.billing.AIRole;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.generation.GenerationJob;
import api.gabaritol.entities.generation.JobStatus;
import api.gabaritol.exceptions.raises.InsufficientCreditsException;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.repositories.exam.ExamRepository;
import api.gabaritol.repositories.generation.GenerationJobRepository;
import api.gabaritol.services.billing.BillingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenerationOrchestratorServiceImpl implements GenerationOrchestratorService {

    private final ExamRepository examRepository;
    private final GenerationJobRepository generationJobRepository;
    private final AsyncGenerationWorker asyncGenerationWorker;
    private final BillingService billingService;

    @Override
    public UUID startGeneration(UUID examId) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new NotFoundException("Exam not found.")
        );

        int worstCaseCost = billingService.calculateCost(
            "gemini-3.5-flash", AIRole.GENERATOR, exam.getQuestionCount()
        );

        if (!billingService.hasSufficientCredits(exam.getUser(), worstCaseCost)) {
            throw new InsufficientCreditsException(
                "Insufficient credits. This generation may cost up to " + worstCaseCost + " credits."
            );
        }

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