package com.heritage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "discussions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monument_id", nullable = false)
    private Monument monument;

    @NotBlank
    @Column(length = 2000)
    private String text;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public Discussion(User user, Monument monument, String text) {
        this.user = user;
        this.monument = monument;
        this.text = text;
        this.timestamp = LocalDateTime.now();
    }
}
