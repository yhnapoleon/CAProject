package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nusiss.t6.caproject.model.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountService {

	List<Discount> getAllDiscounts();

	Page<Discount> getAllDiscounts(Pageable pageable);

	Optional<Discount> getDiscountById(Integer id);

	Discount createDiscount(Discount discount);

	Optional<Discount> updateDiscount(Integer id, Discount discountDetails);

	void deleteDiscount(Integer id);
}


