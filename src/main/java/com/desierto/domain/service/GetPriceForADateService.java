package com.desierto.domain.service;

import com.desierto.domain.Price;
import com.desierto.domain.exception.PriceException;
import java.time.LocalDateTime;

public interface GetPriceForADateService {

  Price execute(LocalDateTime date) throws PriceException;
}
