package api.gabaritol.DTOs.source;

import java.util.UUID;
import api.gabaritol.entities.source.Source;
import api.gabaritol.entities.source.SourceType;

public record SourceResponseDTO(
    UUID id,
    SourceType type,
    String fileName,
    Integer contentLength
) {
    public static SourceResponseDTO fromEntity(Source source) {
        return new SourceResponseDTO(
            source.getId(),
            source.getType(),
            source.getFileName(),
            source.getExtractedContent() != null ? source.getExtractedContent().length() : 0
        );
    }
}