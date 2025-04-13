package com.desierto.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class Price {

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private Integer listing;

  private final Integer priority;

  private BigDecimal amount;

  private Currency currency;

  public Price(LocalDateTime startDate, LocalDateTime endDate, Integer listing, Integer priority,
      BigDecimal amount, Currency currency) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.listing = listing;
    this.priority = priority;
    this.amount = amount;
    this.currency = currency;
  }

  public Price(int priority) {
    this.priority = priority;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public Integer getListing() {
    return listing;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }
}
