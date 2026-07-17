package api.gabaritol.services.source;

import org.springframework.web.multipart.MultipartFile;

import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.source.Source;

public interface SourceService {
    Source uploadPdf(Exam exam, MultipartFile file);
}
