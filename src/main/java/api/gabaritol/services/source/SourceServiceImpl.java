package api.gabaritol.services.source;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.source.Source;
import api.gabaritol.entities.source.SourceType;
import api.gabaritol.exceptions.raises.FileProcessingException;
import api.gabaritol.repositories.source.SourceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService{

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final SourceRepository sourceRepository;
    
    @Override
    public Source uploadPdf(Exam exam, MultipartFile file) {
        validateFile(file);

        String extractedText = extractTextFromPdf(file);

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

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File exceeds maximum size of 10MB.");
        }
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are supported.");
        }
    }

    private String extractTextFromPdf(MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to extract text from PDF.");
        }
    }
    
}
