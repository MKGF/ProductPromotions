package domain;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

  List<Price> findByDate(LocalDateTime date);
}
