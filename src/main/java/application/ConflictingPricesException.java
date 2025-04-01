package application;

public class ConflictingPricesException extends RuntimeException {

  public ConflictingPricesException(Exception e) {
    super(e);
  }
}
