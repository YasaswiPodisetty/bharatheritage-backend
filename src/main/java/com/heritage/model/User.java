package com.heritage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    private String role;

    @Column(name = "joined_at")
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PageVisit> pageVisits = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TourRegistration> tourRegistrations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MonumentVisit> monumentVisits = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Discussion> discussions = new ArrayList<>();

    // Convenience constructor used by DataSeeder (without id)
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
        this.pageVisits = new ArrayList<>();
        this.tourRegistrations = new ArrayList<>();
        this.monumentVisits = new ArrayList<>();
        this.discussions = new ArrayList<>();
    }
}
