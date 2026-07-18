package api.gabaritol.services.generation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import api.gabaritol.ai.gemini.GeneratedQuestionDTO;
import api.gabaritol.ai.gemini.GeneratedQuestionsBatchDTO;
import api.gabaritol.ai.generation.QuestionGeneratorService;
import api.gabaritol.entities.exam.*;
import api.gabaritol.entities.generation.GenerationJob;
import api.gabaritol.entities.generation.JobStatus;
import api.gabaritol.entities.question.AnswerOption;
import api.gabaritol.entities.question.*;
import api.gabaritol.entities.source.Source;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.repositories.exam.ExamRepository;
import api.gabaritol.repositories.generation.GenerationJobRepository;
import api.gabaritol.repositories.question.AnswerOptionRepository;
import api.gabaritol.repositories.question.*;
import api.gabaritol.repositories.source.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncGenerationWorker {

    private final ExamRepository examRepository;
    private final SourceRepository sourceRepository;
    private final QuestionGeneratorService questionGeneratorService;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final GenerationJobRepository generationJobRepository;

    @Async
    public void process(UUID jobId, UUID examId) {
        GenerationJob job = generationJobRepository.findById(jobId)
            .orElseThrow(() -> new NotFoundException("Generation job not found."));

        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new NotFoundException("Exam not found."));

        job.setStatus(JobStatus.GENERATING);
        job.setStartedAt(LocalDateTime.now());
        generationJobRepository.save(job);

        exam.setStatus(ExamStatus.GENERATING);
        examRepository.save(exam);

        try {
            String referenceContent = buildReferenceContent(exam);

            GeneratedQuestionsBatchDTO batch = questionGeneratorService.generateQuestions(
                exam.getTopic(), exam.getBoard(), exam.getDifficulty(),
                exam.getQuestionCount(), referenceContent
            );

            int order = 1;
            int generatedCount = 0;

            for (GeneratedQuestionDTO generated : batch.questions()) {
                Question question = buildQuestionEntity(generated, exam);
                Question savedQuestion = questionRepository.save(question);

                List<AnswerOption> options = generated.options().stream()
                    .map(opt -> buildAnswerOption(opt, savedQuestion))
                    .collect(Collectors.toList());
                answerOptionRepository.saveAll(options);

                linkQuestionToExam(exam, savedQuestion, order++);

                generatedCount++;
                job.setQuestionsGenerated(generatedCount);
                generationJobRepository.save(job);
            }

            exam.setStatus(ExamStatus.COMPLETED);
            examRepository.save(exam);

            job.setStatus(JobStatus.COMPLETED);
            job.setFinishedAt(LocalDateTime.now());
            generationJobRepository.save(job);

            log.info("Exam {} generated successfully with {} questions.", examId, batch.questions().size());

        } catch (Exception e) {
            log.error("Failed to generate questions for exam {}", examId, e);

            exam.setStatus(ExamStatus.FAILED);
            examRepository.save(exam);

            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
            job.setFinishedAt(LocalDateTime.now());
            generationJobRepository.save(job);
        }
    }

    private String buildReferenceContent(Exam exam) {
        List<Source> sources = sourceRepository.findByExam(exam);
        return sources.stream()
            .map(Source::getExtractedContent)
            .filter(content -> content != null && !content.isBlank())
            .collect(Collectors.joining("\n\n---\n\n"));
    }

    private Question buildQuestionEntity(GeneratedQuestionDTO generated, Exam exam) {
        Question question = new Question();
        question.setStatement(generated.statement());
        question.setType(QuestionType.MULTIPLE_CHOICE);
        question.setExplanation(generated.explanation());
        question.setTopic(exam.getTopic());
        question.setBoard(exam.getBoard());
        question.setPosition(exam.getPosition());
        question.setDifficulty(exam.getDifficulty());
        question.setGeneratorModel("gemini-3.5-flash");
        question.setReviewed(false);
        question.setApproved(true);
        question.setTimesUsed(1);

        String correctAnswer = generated.options().stream()
            .filter(GeneratedQuestionDTO.GeneratedOptionDTO::correct)
            .map(GeneratedQuestionDTO.GeneratedOptionDTO::label)
            .findFirst()
            .orElse(null);
        question.setCorrectAnswer(correctAnswer);

        return question;
    }

    private AnswerOption buildAnswerOption(GeneratedQuestionDTO.GeneratedOptionDTO opt, Question question) {
        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setLabel(opt.label());
        option.setText(opt.text());
        option.setCorrect(opt.correct());
        return option;
    }

    private void linkQuestionToExam(Exam exam, Question question, int order) {
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExam(exam);
        examQuestion.setQuestion(question);
        examQuestion.setOrderInExam(order);
        examQuestionRepository.save(examQuestion);
    }
}