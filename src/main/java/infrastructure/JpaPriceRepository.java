package infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPriceRepository extends JpaRepository<DbPrice, Long> {

  @Query(value = "SELECT price FROM DbPrice price WHERE price.startDate < :date AND price.endDate > :date")
  List<DbPrice> findByDate(@Param("date") LocalDateTime date);
}
