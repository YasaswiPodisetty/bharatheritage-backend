package com.heritage.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "page_visits",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "page"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String page;

    @Builder.Default
    private int visitCount = 1;

    @Builder.Default
    private LocalDateTime lastVisited = LocalDateTime.now();

    public PageVisit(User user, String page) {
        this.user = user;
        this.page = page;
        this.visitCount = 1;
        this.lastVisited = LocalDateTime.now();
    }
}
