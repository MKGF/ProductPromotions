package com.desierto.infrastructure.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.desierto.infrastructure.entity.DbPrice;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;

public class TestingUtils {

  public static String getFormattedDate(LocalDateTime date) {
    DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
    return date.format(formatter);
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper()
          .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
          .registerModule(new JavaTimeModule())
          .writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static List<DbPrice> getFixtures() {
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
