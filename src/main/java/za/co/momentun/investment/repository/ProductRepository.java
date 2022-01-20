package za.co.momentun.investment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.momentun.investment.model.Product;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findByType(String type);
}
