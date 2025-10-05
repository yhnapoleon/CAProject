package sg.nusiss.t6.caproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.service.ProductService;
import sg.nusiss.t6.caproject.config.JwtRequestFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @org.springframework.test.context.bean.override.mockito.MockitoBean
  private ProductService productService;

  @Autowired
  private ObjectMapper objectMapper;

  // Mock security-related beans to satisfy WebSecurityConfig constructor
  @org.springframework.test.context.bean.override.mockito.MockitoBean
  private UserDetailsService userDetailsService;

  @org.springframework.test.context.bean.override.mockito.MockitoBean
  private JwtRequestFilter jwtRequestFilter;

  @org.springframework.test.context.bean.override.mockito.MockitoBean
  private PasswordEncoder passwordEncoder;

  private Product sampleProduct(Integer id) {
    Product p = new Product();
    p.setProductId(id);
    p.setProductName("Sample");
    p.setProductDescription("Desc");
    p.setProductPrice(99.0f);
    p.setProductStockQuantity(10);
    p.setProductCategory("Cat");
    p.setIsVisible(1);
    p.setImageUrl("http://img");
    return p;
  }

  @Test
  void getVisibleProducts_returnsList() throws Exception {
    Mockito.when(productService.getAllVisibleProducts())
        .thenReturn(List.of(sampleProduct(1), sampleProduct(2)));

    mockMvc.perform(get("/api/products/getVisibleProducts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].productId", is(1)));
  }

  @Test
  void getVisibleProducts_paged_returnsPage() throws Exception {
    Page<Product> page = new PageImpl<>(List.of(sampleProduct(1)), PageRequest.of(0, 10), 1);
    Mockito.when(productService.getAllVisibleProducts(any(Pageable.class))).thenReturn(page);

    mockMvc.perform(get("/api/products").param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].productId", is(1)));
  }

  @Test
  void getProductById_returnsProduct() throws Exception {
    Mockito.when(productService.getProductById(1)).thenReturn(sampleProduct(1));

    mockMvc.perform(get("/api/products/getProductById/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId", is(1)));
  }

  @Test
  void getReviewsByProductId_returnsList() throws Exception {
    Review r = new Review();
    r.setReviewId(10);
    r.setComment("Nice");
    r.setReviewRank(5);
    r.setReviewCreateTime(LocalDateTime.now());

    Mockito.when(productService.getReviewsByProductId(1)).thenReturn(List.of(r));

    mockMvc.perform(get("/api/products/getReviewsByProductId/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].reviewId", is(10)));
  }

  @Test
  void addReviewToProduct_createsReview() throws Exception {
    Review req = new Review();
    req.setComment("Great");
    req.setReviewRank(4);

    Review saved = new Review();
    saved.setReviewId(11);
    saved.setComment("Great");
    saved.setReviewRank(4);
    saved.setReviewCreateTime(LocalDateTime.now());

    Mockito.when(productService.addReviewToProduct(eq(1), any(Review.class))).thenReturn(saved);

    mockMvc.perform(post("/api/products/addReviewToProduct/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.reviewId", is(11)))
        .andExpect(jsonPath("$.comment", is("Great")));
  }
}
