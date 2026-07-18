package api.gabaritol.services.generation;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import api.gabaritol.ai.gemini.GeneratedQuestionDTO;
import api.gabaritol.ai.gemini.GeneratedQuestionsBatchDTO;
import api.gabaritol.ai.generation.QuestionGeneratorService;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.exam.ExamStatus;
import api.gabaritol.entities.question.AnswerOption;
import api.gabaritol.entities.question.ExamQuestion;
import api.gabaritol.entities.question.Question;
import api.gabaritol.entities.question.QuestionType;
import api.gabaritol.entities.source.Source;
import api.gabaritol.exceptions.raises.NotFoundException;
import api.gabaritol.repositories.exam.ExamRepository;
import api.gabaritol.repositories.question.AnswerOptionRepository;
import api.gabaritol.repositories.question.ExamQuestionRepository;
import api.gabaritol.repositories.question.QuestionRepository;
import api.gabaritol.repositories.source.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenerationOrchestratorServiceImpl implements GenerationOrchestratorService {

    private final ExamRepository examRepository;
    private final SourceRepository sourceRepository;
    private final QuestionGeneratorService questionGeneratorService;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final ExamQuestionRepository examQuestionRepository;

    @Override
    public void generateQuestionsForExam(java.util.UUID examId) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new NotFoundException("Exam not found."));

        exam.setStatus(ExamStatus.GENERATING);
        examRepository.save(exam);

        try {
            String referenceContent = buildReferenceContent(exam);

            GeneratedQuestionsBatchDTO batch = questionGeneratorService.generateQuestions(
                exam.getTopic(), exam.getBoard(), exam.getDifficulty(),
                exam.getQuestionCount(), referenceContent
            );

            int order = 1;
            for (GeneratedQuestionDTO generated : batch.questions()) {
                Question question = buildQuestionEntity(generated, exam);
                Question savedQuestion = questionRepository.save(question);

                List<AnswerOption> options = generated.options().stream()
                    .map(opt -> buildAnswerOption(opt, savedQuestion))
                    .collect(Collectors.toList());
                answerOptionRepository.saveAll(options);

                linkQuestionToExam(exam, savedQuestion, order++);
            }

            exam.setStatus(ExamStatus.COMPLETED);
            examRepository.save(exam);
            log.info("Exam {} generated successfully with {} questions.", examId, batch.questions().size());

        } catch (Exception e) {
            log.error("Failed to generate questions for exam {}", examId, e);
            exam.setStatus(ExamStatus.FAILED);
            examRepository.save(exam);
            throw e;
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