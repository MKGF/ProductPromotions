package infrastructure;

import domain.Price;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public record PriceDto(Long productIdentifier, Long brandIdentifier, Integer listing, String startDate, String endDate, String finalPrice) {

  static DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;

  public static PriceDto fromDomain(Price price, Long productIdentifier, Long brandIdentifier) {
    return new PriceDto(productIdentifier, brandIdentifier, price.getListing(), price.getStartDate().format(formatter), price.getEndDate().format(formatter), getFinalPrice(price));
  }

  private static String getFinalPrice(Price price) {
    return price.getAmount().setScale(2, RoundingMode.HALF_EVEN)
        + price.getCurrency().getCurrencyCode();
  }
}
