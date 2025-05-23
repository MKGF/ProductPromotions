package com.desierto.infrastructure.controller;

import com.desierto.application.exception.ConflictingPricesException;
import com.desierto.domain.service.GetPriceForADateService;
import com.desierto.domain.exception.PriceException;
import com.desierto.domain.exception.PriceNotFoundException;
import com.desierto.infrastructure.exception.InvalidDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/price")
public class PriceController {

  private final GetPriceForADateService getPriceForADateService;

  @Autowired
  public PriceController(GetPriceForADateService getPriceForADateService) {
    this.getPriceForADateService = getPriceForADateService;
  }

  @GetMapping
  public ResponseEntity<PriceDto> getForADate(PriceFilters priceFilters)
      throws PriceException {
    try {
      priceFilters.validate();
      return ResponseEntity.ok(
          PriceDto.fromDomain(
            getPriceForADateService.execute(priceFilters.toDomain()),
            priceFilters.productIdentifier(),
            priceFilters.brandIdentifier()
          )
      );
    } catch (InvalidDateException e) {
      return ResponseEntity.badRequest().build();
    } catch (PriceNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (ConflictingPricesException e) {
      return ResponseEntity.unprocessableEntity().build();
    }
  }
}
