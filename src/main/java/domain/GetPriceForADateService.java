package domain;

import java.time.LocalDateTime;

public interface GetPriceForADateService {

  Price execute(LocalDateTime date) throws PriceException;
}
