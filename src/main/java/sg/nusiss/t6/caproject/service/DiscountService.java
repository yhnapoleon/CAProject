//By Ying Hao
//This feature has not been implemented yet.
package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nusiss.t6.caproject.model.Discount;
import sg.nusiss.t6.caproject.controller.dto.DiscountRequestDTO;

import java.util.List;
import java.util.Optional;

public interface DiscountService {

	List<Discount> getAllDiscounts();

	Page<Discount> getAllDiscounts(Pageable pageable);

	Optional<Discount> getDiscountById(Integer id);

	Discount createDiscount(DiscountRequestDTO discount);

	Optional<Discount> updateDiscount(Integer id, DiscountRequestDTO discountDetails);

	void deleteDiscount(Integer id);
}
