package za.co.momentun.investment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.momentun.investment.model.AuditTrail;


@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long>{

}
