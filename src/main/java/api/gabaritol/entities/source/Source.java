package api.gabaritol.entities.source;

import java.util.UUID;
import api.gabaritol.entities.common.BaseEntity;
import api.gabaritol.entities.exam.Exam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sources")
public class Source extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Exam exam;

    @Enumerated(EnumType.STRING)
    private SourceType type;

    private String fileName;
    private String originalUrl;
    private String storagePath;

    @Column(columnDefinition = "TEXT")
    private String extractedContent;
}