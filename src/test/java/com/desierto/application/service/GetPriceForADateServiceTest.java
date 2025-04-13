package com.desierto.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.desierto.application.exception.ConflictingPricesException;
import com.desierto.domain.Price;
import com.desierto.domain.exception.PriceException;
import com.desierto.domain.exception.PriceNotFoundException;
import com.desierto.domain.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPriceForADateServiceTest {

  @Mock
  PriceRepository priceRepository;

  GetPriceForADateService cut;

  @BeforeEach
  void setUp() {
    cut = new GetPriceForADateService(priceRepository);
  }

  @Test
  void givenADate_forVariousPrices_returnsAPrice() throws PriceException {
    // Given
    Price aHighPriorityPrice = new Price(3);
    Price aLowerPriorityPrice = new Price(2);
    Price anEvenLowerPriorityPrice = new Price(1);
    Price lowestPriorityPrice = new Price(0);
    List<Price> prices = List.of(aLowerPriorityPrice, anEvenLowerPriorityPrice, lowestPriorityPrice, aHighPriorityPrice);
    when(priceRepository.findByDate(any())).thenReturn(prices);

    // When
    Price result = cut.execute(LocalDateTime.now());

    // Then
    assertEquals(aHighPriorityPrice, result);
  }

  @Test
  void givenADate_forNoPrices_throwsException() {
    // Given
    List<Price> prices = List.of();
    when(priceRepository.findByDate(any())).thenReturn(prices);

    // When Then
    assertThrows(PriceNotFoundException.class, () -> cut.execute(LocalDateTime.now()));
  }

  @Test
  void givenADate_forVariousPricesWithSamePriority_throwsException() {
    // Given
    Price aHighPriorityPrice = new Price(3);
    Price anotherHighPriorityPrice = new Price(3);
    Price aLowerPriorityPrice = new Price(1);
    Price lowestPriorityPrice = new Price(0);
    List<Price> prices = List.of(aLowerPriorityPrice, anotherHighPriorityPrice, lowestPriorityPrice, aHighPriorityPrice);
    when(priceRepository.findByDate(any())).thenReturn(prices);

    // When Then
    assertThrows(ConflictingPricesException.class, () -> cut.execute(LocalDateTime.now()));
  }

  @Test
  void givenADate_forAPrice_returnsAPrice() throws PriceException {
    // Given
    Price aPrice = new Price(1);
    List<Price> prices = List.of(aPrice);
    when(priceRepository.findByDate(any())).thenReturn(prices);

    // When
    Price result = cut.execute(LocalDateTime.now());

    // Then
    assertEquals(aPrice, result);
  }
}