package infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceController.class)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
@Import({TestConfig.class, PriceController.class})
class PriceControllerTest {

  private final JpaPriceRepository priceRepository;

  private final MockMvc mockMvc;

  @Autowired
  PriceControllerTest(JpaPriceRepository priceRepository, MockMvc mockMvc) {
    this.priceRepository = priceRepository;
    this.mockMvc = mockMvc;
  }

  @BeforeEach
  public void setUp() {
    // Initialize test data before each test method
    Long brandId = 1L;
    Long productId = 35455L;
    DbPrice defaultDbPrice = new DbPrice(
        brandId,
        LocalDateTime.of(2020, 6, 14, 0, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        1L,
        productId,
        0,
        BigDecimal.valueOf(35.5),
        Currency.getInstance("EUR")
    );
    DbPrice firstPromoDbPrice = new DbPrice(
        brandId,
        LocalDateTime.of(2020, 6, 14, 15, 0, 0),
        LocalDateTime.of(2020, 6, 14, 18, 30, 0),
        2L,
        productId,
        1,
        BigDecimal.valueOf(25.45),
        Currency.getInstance("EUR")
    );
    DbPrice secondPromoDbPrice = new DbPrice(
        brandId,
        LocalDateTime.of(2020, 6, 15, 0, 0, 0),
        LocalDateTime.of(2020, 6, 15, 11, 0, 0),
        3L,
        productId,
        1,
        BigDecimal.valueOf(30.5),
        Currency.getInstance("EUR")
    );
    DbPrice thirdPromoDbPrice = new DbPrice(
        brandId,
        LocalDateTime.of(2020, 6, 15, 16, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        4L,
        productId,
        1,
        BigDecimal.valueOf(38.95),
        Currency.getInstance("EUR")
    );
    priceRepository.saveAll(List.of(defaultDbPrice, firstPromoDbPrice, secondPromoDbPrice, thirdPromoDbPrice));
  }

  @AfterEach
  public void tearDown() {
    // Release test data after each test method
    priceRepository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("datesAndPrices")
  public void givenADate_returnsCorrectPricingForListing(LocalDateTime dateFilter, String finalPrice) throws Exception {
    PriceFilters filters = new PriceFilters(dateFilter, 35455L, 1L);
    mockMvc.perform(post("/price").content(asJsonString(filters))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.finalPrice", Matchers.is(finalPrice)));
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper()
          .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
          .registerModule(new JavaTimeModule())
          .writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static List<Arguments> datesAndPrices() {
    return List.of(
        Arguments.of(LocalDateTime.of(2020, 6, 14, 10, 0, 0), "35.50EUR"),
        Arguments.of(LocalDateTime.of(2020, 6, 14, 16, 0, 0), "25.45EUR"),
        Arguments.of(LocalDateTime.of(2020, 6, 14, 21, 0, 0), "35.50EUR"),
        Arguments.of(LocalDateTime.of(2020, 6, 15, 10, 0, 0), "30.50EUR"),
        Arguments.of(LocalDateTime.of(2020, 6, 16, 21, 0, 0), "38.95EUR")
    );
  }

}