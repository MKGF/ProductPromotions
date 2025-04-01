package infrastructure;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;

@JsonSerialize
public record PriceFilters(
    @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime applicationDate,
    long productIdentifier,
    long brandIdentifier
) {

  public LocalDateTime toDomain() {
    return applicationDate;
  }
}
