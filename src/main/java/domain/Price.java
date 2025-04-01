package domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class Price {

  private Brand brand;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private Integer listing;

  private Product product;

  private Integer priority;

  private BigDecimal amount;

  private Currency currency;

  public Price(int priority) {

  }

  public Price desambiguateWith(Price otherPrice) {
    return otherPrice;
  }
}
