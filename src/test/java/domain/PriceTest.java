package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PriceTest {

  @Test
  public void desambiguates() {
    Price price = new Price(0);
    Price otherPrice = new Price(1);
    assertEquals(price.desambiguateWith(otherPrice), otherPrice);
  }

  @Test
  public void conflicts() {

  }
}
