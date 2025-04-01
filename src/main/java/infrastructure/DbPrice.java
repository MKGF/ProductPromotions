package infrastructure;

import domain.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRICES")
public class DbPrice {

  @Column
  private Long brandId;

  @Column
  private LocalDateTime startDate;

  @Column
  private LocalDateTime endDate;

  @Id
  private final Long listing;

  @Column
  private Long productId;

  @Column
  private Integer priority;

  @Column
  private BigDecimal amount;

  @Column
  private Currency currency;

  public DbPrice(Long brandId, LocalDateTime startDate, LocalDateTime endDate, Long listing,
      Long productId, Integer priority, BigDecimal amount, Currency currency) {
    this.brandId = brandId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.listing = listing;
    this.productId = productId;
    this.priority = priority;
    this.amount = amount;
    this.currency = currency;
  }

  public DbPrice() {
    this.listing = 0L;
  }

  public Long getBrand() {
    return brandId;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public Long getListing() {
    return listing;
  }

  public Long getProduct() {
    return productId;
  }

  public Integer getPriority() {
    return priority;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setBrand(Long brandId) {
    this.brandId = brandId;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public void setProduct(Long productId) {
    this.productId = productId;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Price toDomain() {
    return new Price(this.startDate, this.endDate, Math.toIntExact(this.listing), this.priority, this.amount, this.currency);
  }
}
