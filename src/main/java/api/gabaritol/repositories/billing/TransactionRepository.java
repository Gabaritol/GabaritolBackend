package api.gabaritol.repositories.billing;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.gabaritol.entities.billing.Transaction;
import api.gabaritol.entities.exam.Exam;
import api.gabaritol.entities.user.User;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserOrderByCreatedAtDesc(User user);
    @Modifying
    @Query("UPDATE Transaction t SET t.relatedExam = null WHERE t.relatedExam = :exam")
    void nullifyRelatedExam(@Param("exam") Exam exam);
}