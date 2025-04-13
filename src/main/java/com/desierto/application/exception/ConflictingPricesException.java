package com.desierto.application.exception;

public class ConflictingPricesException extends RuntimeException {

  public ConflictingPricesException(Exception e) {
    super(e);
  }
}
