package com.heritage.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tour_registrations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "monument_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourRegistration {

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
    private LocalDateTime registeredAt = LocalDateTime.now();

    public TourRegistration(User user, Monument monument) {
        this.user = user;
        this.monument = monument;
        this.registeredAt = LocalDateTime.now();
    }
}
