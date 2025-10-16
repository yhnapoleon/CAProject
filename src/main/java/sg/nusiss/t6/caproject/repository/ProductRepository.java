package sg.nusiss.t6.caproject.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    Page<Product> findByIsVisible(Integer isVisible, Pageable pageable);


    List<Product> findByIsVisible(Integer isVisible);
}