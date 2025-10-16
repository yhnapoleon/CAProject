//By Ying Hao
//This feature has not been implemented yet.
package sg.nusiss.t6.caproject.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Discount;
import sg.nusiss.t6.caproject.service.DiscountService;
import sg.nusiss.t6.caproject.controller.dto.DiscountRequestDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/discounts")
public class AdminDiscountController {

	private final DiscountService discountService;

	public AdminDiscountController(DiscountService discountService) {
		this.discountService = discountService;
	}

	@GetMapping("/getAllDiscounts")
	public ResponseEntity<List<Discount>> getAllDiscounts() {
		return ResponseEntity.ok(discountService.getAllDiscounts());
	}

	@GetMapping
	public ResponseEntity<Page<Discount>> getAllDiscountsPaged(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(discountService.getAllDiscounts(pageable));
	}

	@GetMapping("/getDiscount/{id}")
	public ResponseEntity<?> getDiscount(@PathVariable Integer id) {
		Optional<Discount> d = discountService.getDiscountById(id);
		return d.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/createDiscount")
	public ResponseEntity<Discount> createDiscount(@RequestBody DiscountRequestDTO discount) {
		Discount created = discountService.createDiscount(discount);
		return ResponseEntity.status(201).body(created);
	}

	@PutMapping("/updateDiscount/{id}")
	public ResponseEntity<?> updateDiscount(@PathVariable Integer id, @RequestBody DiscountRequestDTO discount) {
		Optional<Discount> updated = discountService.updateDiscount(id, discount);
		return updated.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/deleteDiscount/{id}")
	public ResponseEntity<Void> deleteDiscount(@PathVariable Integer id) {
		discountService.deleteDiscount(id);
		return ResponseEntity.noContent().build();
	}
}
