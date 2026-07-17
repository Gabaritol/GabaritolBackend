package api.gabaritol.services.source;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.source.Source;
import api.gabaritol.entities.source.SourceType;
import api.gabaritol.repositories.source.SourceRepository;
import api.gabaritol.util.FileProcessorComponent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService{

    private final SourceRepository sourceRepository;
    private final FileProcessorComponent fileProcessor;
    
    @Override
    public Source uploadPdf(Exam exam, MultipartFile file) {
        fileProcessor.validatePdf(file);
        String extractedText = fileProcessor.extractTextFromPdf(file);

        Source source = new Source();
        source.setExam(exam);
        source.setType(SourceType.PDF);
        source.setFileName(file.getOriginalFilename());
        source.setExtractedContent(extractedText);

        return sourceRepository.save(source);
    }

    @Override
    public Source addPastedText(Exam exam, String text) {
        Source source = new Source();
        source.setExam(exam);
        source.setType(SourceType.PASTED_TEXT);
        source.setExtractedContent(text);

        return sourceRepository.save(source);
    }

    @Override
    public List<Source> findByExam(Exam exam) {
        return sourceRepository.findByExam(exam);
    }
}
