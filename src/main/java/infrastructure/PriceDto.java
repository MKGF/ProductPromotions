package infrastructure;

import domain.Price;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record PriceDto(Long productIdentifier, Long brandIdentifier, Integer listing, LocalDateTime startDate, LocalDateTime endDate, String finalPrice) {

  public static PriceDto fromDomain(Price price, Long productIdentifier, Long brandIdentifier) {
    return new PriceDto(productIdentifier, brandIdentifier, price.getListing(), price.getStartDate(), price.getEndDate(), getFinalPrice(price));
  }

  private static String getFinalPrice(Price price) {
    return price.getAmount().setScale(2, RoundingMode.HALF_EVEN)
        + price.getCurrency().getCurrencyCode();
  }
}
