package infrastructure;

import domain.GetPriceForADateService;
import domain.NotDesambiguableException;
import domain.Price;
import domain.PriceException;
import domain.PriceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PostMapping
  public ResponseEntity<PriceDto> getForADate(@RequestBody PriceFilters priceFilters)
      throws PriceException {
    try {
      return ResponseEntity.ok(
          PriceDto.fromDomain(
            getPriceForADateService.execute(priceFilters.toDomain()),
            priceFilters.productIdentifier(),
            priceFilters.brandIdentifier()
          )
      );
    } catch (PriceNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (NotDesambiguableException e) {
      return ResponseEntity.unprocessableEntity().build();
    }
  }

  @GetMapping("test")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("hello!");
  }
}
