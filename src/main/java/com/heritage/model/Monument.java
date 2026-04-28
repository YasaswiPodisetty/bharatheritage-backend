package com.heritage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monuments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Monument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String city;
    private String state;
    private String era;
    private String year;
    private String category;

    @Column(length = 1000)
    private String description;

    private String image;
    private String thumbnail;

    @Column(length = 500)
    private String facts;      // pipe-separated

    @Column(name = "tour_points", length = 500)
    private String tourPoints; // pipe-separated

    @OneToMany(mappedBy = "monument", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TourRegistration> tourRegistrations = new ArrayList<>();

    @OneToMany(mappedBy = "monument", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MonumentVisit> monumentVisits = new ArrayList<>();

    @OneToMany(mappedBy = "monument", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Discussion> discussions = new ArrayList<>();
}
