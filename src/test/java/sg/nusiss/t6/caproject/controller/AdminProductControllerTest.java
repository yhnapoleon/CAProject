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
import sg.nusiss.t6.caproject.service.ProductService;
import sg.nusiss.t6.caproject.config.JwtRequestFilter;

import java.util.List;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminProductControllerTest {

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
    p.setProductName("AdminSample");
    p.setProductDescription("Desc");
    p.setProductPrice(new BigDecimal("10.0"));
    p.setProductStockQuantity(5);
    p.setProductCategory("Cat");
    p.setIsVisible(1);
    return p;
  }

  @Test
  void getAllProducts_returnsList() throws Exception {
    Mockito.when(productService.getAllProducts()).thenReturn(List.of(sampleProduct(1)));
    mockMvc.perform(get("/api/admin/products/getAllProducts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].productId", is(1)));
  }

  @Test
  void getAllProducts_paged_returnsPage() throws Exception {
    Page<Product> page = new PageImpl<>(List.of(sampleProduct(1)), PageRequest.of(0, 10), 1);
    Mockito.when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

    mockMvc.perform(get("/api/admin/products").param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].productId", is(1)));
  }

  @Test
  void createProduct_returnsCreated() throws Exception {
    Product req = sampleProduct(null);
    Product saved = sampleProduct(2);

    Mockito.when(productService.createProduct(any(Product.class))).thenReturn(saved);

    mockMvc.perform(post("/api/admin/products/createProduct")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.productId", is(2)));
  }

  @Test
  void updateProduct_returnsOk() throws Exception {
    Product req = sampleProduct(null);
    Product updated = sampleProduct(1);

    Mockito.when(productService.updateProduct(eq(1), any(Product.class))).thenReturn(Optional.of(updated));

    mockMvc.perform(put("/api/admin/products/updateProduct/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId", is(1)));
  }

  @Test
  void updateStock_acceptsBothKeys() throws Exception {
    Product updated = sampleProduct(1);
    updated.setProductStockQuantity(99);
    Mockito.when(productService.updateStock(eq(1), eq(99))).thenReturn(Optional.of(updated));

    // stock
    mockMvc.perform(patch("/api/admin/products/updateStock/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Map.of("stock", 99))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productStockQuantity", is(99)));

    // stockQuantity
    Mockito.when(productService.updateStock(eq(1), eq(99))).thenReturn(Optional.of(updated));
    mockMvc.perform(patch("/api/admin/products/updateStock/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Map.of("stockQuantity", 99))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productStockQuantity", is(99)));
  }

  @Test
  void setVisibility_returnsOk() throws Exception {
    Product updated = sampleProduct(1);
    updated.setIsVisible(0);
    Mockito.when(productService.setProductVisibility(1, false)).thenReturn(Optional.of(updated));

    mockMvc.perform(patch("/api/admin/products/setVisibility/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Map.of("isVisible", false))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isVisible", is(0)));
  }
}
