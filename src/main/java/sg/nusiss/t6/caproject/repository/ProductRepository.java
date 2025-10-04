package sg.nusiss.t6.caproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 查找所有可见的商品并进行分页
     * @param isVisible 是否可见
     * @param pageable 分页信息
     * @return 分页后的商品列表
     */
    Page<Product> findByIsVisible(boolean isVisible, Pageable pageable);
}