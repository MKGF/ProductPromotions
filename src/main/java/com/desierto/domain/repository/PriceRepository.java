package com.desierto.domain.repository;

import com.desierto.domain.Price;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

  List<Price> findByDate(LocalDateTime date);
}
