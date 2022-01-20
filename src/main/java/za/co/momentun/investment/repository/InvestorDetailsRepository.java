package za.co.momentun.investment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.momentun.investment.model.InvestorDetails;

import java.util.Optional;


@Repository
public interface InvestorDetailsRepository extends JpaRepository<InvestorDetails, Long>{
   Optional<InvestorDetails> findByName(String playerName);

}
