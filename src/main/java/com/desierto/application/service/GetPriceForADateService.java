package com.desierto.application.service;

import com.desierto.domain.Price;
import com.desierto.domain.repository.PriceRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPriceForADateService implements com.desierto.domain.service.GetPriceForADateService {

  private final PriceRepository priceRepository;

  @Autowired
  public GetPriceForADateService(PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public Price execute(LocalDateTime date) {
    return priceRepository.findByDate(date);
  }
}
