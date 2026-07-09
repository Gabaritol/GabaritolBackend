package api.gabaritol.entities.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.gabaritol.entities.common.BaseEntity;
import api.gabaritol.entities.exam.Exam;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String email;
    private String password;

    private String verificationCode;
    private boolean verified = false;
    private int verificationAttempts = 0;
    
    private LocalDateTime codeExpiresAt;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private PlanType plan;

    private Integer availableCredits;

    @OneToMany(mappedBy = "user")
    private List<Exam> exams;

    public boolean isVerificationCodeExpired() {
        if (this.codeExpiresAt == null) return true;
        return LocalDateTime.now().isAfter(this.codeExpiresAt);
    }
}