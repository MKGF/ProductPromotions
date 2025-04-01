package domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class Price implements Comparable<Price> {

  private Brand brand;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private Integer listing;

  private Product product;

  private Integer priority;

  private BigDecimal amount;

  private Currency currency;

  public Price(int priority) {
    this.priority = priority;
  }

  public Price desambiguateWith(Price otherPrice) throws NotDesambiguableException {
    return switch (this.compareTo(otherPrice)) {
      case -1 -> otherPrice;
      case 1 -> this;
      default -> throw new NotDesambiguableException();
    };
  }

  @Override
  public int compareTo(Price other) {
    return this.priority.compareTo(other.priority);
  }
}
