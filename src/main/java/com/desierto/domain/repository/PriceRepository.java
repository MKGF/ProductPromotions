package com.desierto.domain.repository;

import com.desierto.domain.Price;
import com.desierto.domain.exception.PriceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

  Price findByDate(LocalDateTime date) throws PriceNotFoundException;
}
