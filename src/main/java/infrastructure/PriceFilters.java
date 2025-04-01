package infrastructure;

import java.time.LocalDateTime;

public record PriceFilters(LocalDateTime applicationDate, Long productIdentifier, Long brandIdentifier) {

  public LocalDateTime toDomain() {
    return applicationDate;
  }
}
