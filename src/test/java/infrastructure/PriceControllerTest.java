package infrastructure;

import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.test.web.servlet.ResultActions;

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
    priceRepository.saveAll(getFixtures());
  }

  @AfterEach
  public void tearDown() {
    priceRepository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("datesPricesAndListings")
  public void givenADate_returnsCorrectPricingForListing(LocalDateTime dateFilter, String finalPrice, Long listing) throws Exception {
    PriceFilters filters = new PriceFilters(dateFilter, 35455L, 1L);
    DbPrice dbPrice = priceRepository.findById(listing).get();
    ResultActions resultActions = mockMvc.perform(post("/price").content(asJsonString(filters))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    matchesPrice(resultActions, finalPrice, dbPrice);
  }

  @Test
  public void givenEmptyDate_returnsBadRequest() throws Exception {
    PriceFilters filters = new PriceFilters(null, 35455L, 1L);
    mockMvc.perform(post("/price").content(asJsonString(filters))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void givenRandomNonExistingData_returnsNotFound() throws Exception {
    PriceFilters filters = new PriceFilters(LocalDateTime.now(), 123L, 456L);
    mockMvc.perform(post("/price").content(asJsonString(filters))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  private void matchesPrice(ResultActions resultActions, String finalPrice, DbPrice dbPrice)
      throws Exception {
    resultActions.andExpect(jsonPath("$.finalPrice", is(finalPrice)))
        .andExpect(jsonPath("$.productIdentifier", is(toIntExact(dbPrice.getProductId()))))
        .andExpect(jsonPath("$.brandIdentifier", is(toIntExact(dbPrice.getBrandId()))))
        .andExpect(jsonPath("$.listing", is(toIntExact(dbPrice.getListing()))))
        .andExpect(jsonPath("$.startDate", is(getFormattedDate(dbPrice.getStartDate()))))
        .andExpect(jsonPath("$.endDate",is(getFormattedDate(dbPrice.getEndDate()))));
  }

  private static String getFormattedDate(LocalDateTime date) {
    DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
    return date.format(formatter);
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

  private static List<Arguments> datesPricesAndListings() {
    return List.of(
        Arguments.of(LocalDateTime.of(2020, 6, 14, 10, 0, 0), "35.50EUR", 1L),
        Arguments.of(LocalDateTime.of(2020, 6, 14, 16, 0, 0), "25.45EUR", 2L),
        Arguments.of(LocalDateTime.of(2020, 6, 14, 21, 0, 0), "35.50EUR", 1L),
        Arguments.of(LocalDateTime.of(2020, 6, 15, 10, 0, 0), "30.50EUR", 3L),
        Arguments.of(LocalDateTime.of(2020, 6, 16, 21, 0, 0), "38.95EUR", 4L)
    );
  }

  private static List<DbPrice> getFixtures() {
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
    return List.of(defaultDbPrice, firstPromoDbPrice, secondPromoDbPrice, thirdPromoDbPrice);
  }

}