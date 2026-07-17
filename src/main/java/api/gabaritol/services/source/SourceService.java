package api.gabaritol.services.source;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.source.Source;

public interface SourceService {
    Source uploadPdf(Exam exam, MultipartFile file);
    Source addPastedText(Exam exam, String text);
    List<Source> findByExam(Exam exam);
}
