package com.heritage.repository;

import com.heritage.model.MonumentVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonumentVisitRepository extends JpaRepository<MonumentVisit, Long> {
    List<MonumentVisit> findByUserId(Long userId);
    boolean existsByUserIdAndMonumentId(Long userId, Long monumentId);
    List<MonumentVisit> findByMonumentId(Long monumentId);
}
