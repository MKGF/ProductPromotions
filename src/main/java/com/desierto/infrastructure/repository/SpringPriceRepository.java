package com.desierto.infrastructure.repository;

import com.desierto.domain.Price;
import com.desierto.domain.repository.PriceRepository;
import com.desierto.infrastructure.entity.DbPrice;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringPriceRepository implements PriceRepository {

  private final JpaPriceRepository jpaPriceRepository;

  @Autowired
  public SpringPriceRepository(JpaPriceRepository jpaPriceRepository) {
    this.jpaPriceRepository = jpaPriceRepository;
  }

  @Override
  public List<Price> findByDate(LocalDateTime date) {
    return jpaPriceRepository.findByDate(date).stream().map(DbPrice::toDomain).collect(Collectors.toList());
  }
}
