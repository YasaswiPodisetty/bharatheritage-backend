package com.heritage.repository;

import com.heritage.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    // monument.id navigates through the @ManyToOne Monument relationship
    List<Discussion> findByMonumentIdOrderByTimestampAsc(Long monumentId);
    List<Discussion> findByUserId(Long userId);
}
