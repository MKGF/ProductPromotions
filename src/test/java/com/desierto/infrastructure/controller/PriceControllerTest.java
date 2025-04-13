package com.desierto.infrastructure.controller;

import static com.desierto.infrastructure.utils.TestingUtils.getFixtures;
import static com.desierto.infrastructure.utils.TestingUtils.getFormattedDate;
import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.desierto.infrastructure.entity.DbPrice;
import com.desierto.infrastructure.repository.JpaPriceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
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
    ResultActions resultActions = mockMvc.perform(
            get("/price")
                .queryParam("applicationDate", filters.applicationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .queryParam("productIdentifier", String.valueOf(filters.productIdentifier()))
                .queryParam("brandIdentifier", String.valueOf(filters.brandIdentifier()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    matchesPrice(resultActions, finalPrice, dbPrice);
  }

  @Test
  public void givenEmptyDate_returnsBadRequest() throws Exception {
    PriceFilters filters = new PriceFilters(null, 35455L, 1L);
    mockMvc.perform(
            get("/price")
                .queryParam("applicationDate", "")
                .queryParam("productIdentifier", String.valueOf(filters.productIdentifier()))
                .queryParam("brandIdentifier", String.valueOf(filters.brandIdentifier()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void givenRandomNonExistingData_returnsNotFound() throws Exception {
    PriceFilters filters = new PriceFilters(LocalDateTime.now(), 123L, 456L);
    mockMvc.perform(
            get("/price")
                .queryParam("applicationDate", filters.applicationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .queryParam("productIdentifier", String.valueOf(filters.productIdentifier()))
                .queryParam("brandIdentifier", String.valueOf(filters.brandIdentifier()))
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



  private static List<Arguments> datesPricesAndListings() {
    return List.of(
        Arguments.of(LocalDateTime.of(2020, 6, 14, 10, 0, 0), "35.50EUR", 1L),
        Arguments.of(LocalDateTime.of(2020, 6, 14, 16, 0, 0), "25.45EUR", 2L),
        Arguments.of(LocalDateTime.of(2020, 6, 14, 21, 0, 0), "35.50EUR", 1L),
        Arguments.of(LocalDateTime.of(2020, 6, 15, 10, 0, 0), "30.50EUR", 3L),
        Arguments.of(LocalDateTime.of(2020, 6, 16, 21, 0, 0), "38.95EUR", 4L)
    );
  }
}