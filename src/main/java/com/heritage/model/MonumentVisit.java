package com.heritage.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monument_visits",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "monument_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonumentVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monument_id", nullable = false)
    private Monument monument;

    @Builder.Default
    private LocalDateTime visitedAt = LocalDateTime.now();

    public MonumentVisit(User user, Monument monument) {
        this.user = user;
        this.monument = monument;
        this.visitedAt = LocalDateTime.now();
    }
}
