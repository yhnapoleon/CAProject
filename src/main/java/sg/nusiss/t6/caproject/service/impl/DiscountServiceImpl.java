package sg.nusiss.t6.caproject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.Discount;
import sg.nusiss.t6.caproject.repository.DiscountRepository;
import sg.nusiss.t6.caproject.service.DiscountService;
import sg.nusiss.t6.caproject.controller.dto.DiscountRequestDTO;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

	private final DiscountRepository discountRepository;

	public DiscountServiceImpl(DiscountRepository discountRepository) {
		this.discountRepository = discountRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Discount> getAllDiscounts() {
		return discountRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Discount> getAllDiscounts(Pageable pageable) {
		return discountRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Discount> getDiscountById(Integer id) {
		return discountRepository.findById(id);
	}

	@Override
	@Transactional
	public Discount createDiscount(DiscountRequestDTO discount) {
		Discount entity = new Discount();
		entity.setDiscountName(discount.getDiscountName());
		entity.setDiscountDescription(discount.getDiscountDescription());
		entity.setDiscountStartTime(discount.getDiscountStartTime());
		entity.setDiscountEndTime(discount.getDiscountEndTime());
		entity.setDiscountDiscount(discount.getDiscountDiscount());
		return discountRepository.save(entity);
	}

	@Override
	@Transactional
	public Optional<Discount> updateDiscount(Integer id, DiscountRequestDTO discountDetails) {
		return discountRepository.findById(id).map(existing -> {
			existing.setDiscountName(discountDetails.getDiscountName());
			existing.setDiscountDescription(discountDetails.getDiscountDescription());
			existing.setDiscountStartTime(discountDetails.getDiscountStartTime());
			existing.setDiscountEndTime(discountDetails.getDiscountEndTime());
			existing.setDiscountDiscount(discountDetails.getDiscountDiscount());
			return discountRepository.save(existing);
		});
	}

	@Override
	@Transactional
	public void deleteDiscount(Integer id) {
		discountRepository.deleteById(id);
	}
}
