package com.desierto.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.desierto.infrastructure.exception.InvalidDateException;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@JsonSerialize
public record PriceFilters(
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime applicationDate,
    long productIdentifier,
    long brandIdentifier
) {

  public LocalDateTime toDomain() {
    return applicationDate;
  }

  public void validate() throws InvalidDateException {
    if (applicationDate == null) {
      throw new InvalidDateException();
    }
  }
}
