package com.heritage.repository;

import com.heritage.model.TourRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TourRegistrationRepository extends JpaRepository<TourRegistration, Long> {
    List<TourRegistration> findByUserId(Long userId);
    boolean existsByUserIdAndMonumentId(Long userId, Long monumentId);
    List<TourRegistration> findByMonumentId(Long monumentId);
}
