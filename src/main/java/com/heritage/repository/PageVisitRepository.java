package com.heritage.repository;

import com.heritage.model.PageVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PageVisitRepository extends JpaRepository<PageVisit, Long> {
    List<PageVisit> findByUserId(Long userId);
    Optional<PageVisit> findByUserIdAndPage(Long userId, String page);
}
