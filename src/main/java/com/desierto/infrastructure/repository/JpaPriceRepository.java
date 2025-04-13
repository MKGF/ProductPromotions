package com.desierto.infrastructure.repository;

import com.desierto.infrastructure.entity.DbPrice;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPriceRepository extends JpaRepository<DbPrice, Long> {

  @Query(value = "SELECT * FROM PRICES WHERE START_DATE < :date AND END_DATE > :date ORDER BY PRIORITY DESC LIMIT 1", nativeQuery = true)
  DbPrice findByDate(@Param("date") LocalDateTime date);
}
