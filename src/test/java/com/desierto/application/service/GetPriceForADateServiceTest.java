package com.desierto.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.desierto.domain.Price;
import com.desierto.domain.repository.PriceRepository;
import java.time.LocalDateTime;
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
  public void setUp() {
    cut = new GetPriceForADateService(priceRepository);
  }

  @Test
  public void givenADate_forVariousPrices_returnsAPrice() {
    // Given
    Price aPrice = new Price(1);
    when(priceRepository.findByDate(any())).thenReturn(aPrice);

    // When
    Price result = cut.execute(LocalDateTime.now());

    // Then
    assertEquals(aPrice, result);
  }
}