package com.desierto.application.service;

import com.desierto.application.exception.ConflictingPricesException;
import com.desierto.domain.exception.NotDesambiguableException;
import com.desierto.domain.Price;
import com.desierto.domain.exception.PriceException;
import com.desierto.domain.exception.PriceNotFoundException;
import com.desierto.domain.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPriceForADateService implements com.desierto.domain.service.GetPriceForADateService {

  private final PriceRepository priceRepository;

  @Autowired
  public GetPriceForADateService(PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public Price execute(LocalDateTime date) throws PriceException {
    List<Price> prices = priceRepository.findByDate(date);
    if (prices.size() == 1) {
      return prices.get(0);
    }
    return prices.stream().reduce((price, otherPrice) -> {
      try {
        return price.desambiguateWith(otherPrice);
      } catch (NotDesambiguableException e) {
        throw new ConflictingPricesException(e);
      }
    }).orElseThrow(PriceNotFoundException::new);
  }
}
