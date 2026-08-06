package api.gabaritol.repositories.billing;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import api.gabaritol.entities.billing.CreditPackage;

public interface CreditPackageRepository extends JpaRepository<CreditPackage, UUID> {
    List<CreditPackage> findByActiveTrue();
}