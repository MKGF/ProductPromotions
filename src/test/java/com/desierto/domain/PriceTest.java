package com.desierto.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.desierto.domain.exception.NotDesambiguableException;
import org.junit.jupiter.api.Test;

public class PriceTest {

  @Test
  public void desambiguatesWithOther() throws NotDesambiguableException {
    Price price = new Price(0);
    Price otherPrice = new Price(1);
    assertEquals(price.desambiguateWith(otherPrice), otherPrice);
  }

  @Test
  public void desambiguatesWithThis() throws NotDesambiguableException {
    Price price = new Price(1);
    Price otherPrice = new Price(0);
    assertEquals(price.desambiguateWith(otherPrice), price);
  }

  @Test
  public void conflicts() {
    Price price = new Price(1);
    Price otherPrice = new Price(1);
    assertThrows(NotDesambiguableException.class, () -> price.desambiguateWith(otherPrice));
  }
}
