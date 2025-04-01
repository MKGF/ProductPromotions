package infrastructure;

import domain.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "PRICES")
@Getter
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

  public Price toDomain() {
    return new Price(this.startDate, this.endDate, Math.toIntExact(this.listing), this.priority, this.amount, this.currency);
  }
}
