package com.heritage.controller;

import com.heritage.dto.*;
import com.heritage.model.*;
import com.heritage.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final PageVisitRepository        pageVisitRepo;
    private final TourRegistrationRepository tourRegRepo;
    private final MonumentVisitRepository    monumentVisitRepo;
    private final UserRepository             userRepo;
    private final MonumentRepository         monumentRepo;

    // ── Page Visits ───────────────────────────────────────────────────────────

    @PostMapping("/page-visit")
    public ResponseEntity<?> trackPageVisit(@RequestBody Map<String, Object> body) {
        Long   userId = Long.valueOf(body.get("userId").toString());
        String page   = body.get("page").toString();

        return userRepo.findById(userId).map(user -> {
            Optional<PageVisit> existing = pageVisitRepo.findByUserIdAndPage(userId, page);
            if (existing.isPresent()) {
                PageVisit pv = existing.get();
                pv.setVisitCount(pv.getVisitCount() + 1);
                pv.setLastVisited(LocalDateTime.now());
                pageVisitRepo.save(pv);
                return ResponseEntity.ok(Map.of("page", page, "visitCount", pv.getVisitCount()));
            }
            pageVisitRepo.save(new PageVisit(user, page));
            return ResponseEntity.ok(Map.of("page", page, "visitCount", 1));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/page-visits/{userId}")
    public ResponseEntity<?> getPageVisits(@PathVariable Long userId) {
        Map<String, Integer> result = new LinkedHashMap<>();
        pageVisitRepo.findByUserId(userId).forEach(v -> result.put(v.getPage(), v.getVisitCount()));
        return ResponseEntity.ok(result);
    }

    // ── Tour Registrations ────────────────────────────────────────────────────

    @PostMapping("/tour-registration")
    public ResponseEntity<?> registerTour(@RequestBody Map<String, Object> body) {
        Long userId     = Long.valueOf(body.get("userId").toString());
        Long monumentId = Long.valueOf(body.get("monumentId").toString());

        Optional<User>     userOpt = userRepo.findById(userId);
        Optional<Monument> monOpt  = monumentRepo.findById(monumentId);
        if (userOpt.isEmpty() || monOpt.isEmpty()) return ResponseEntity.notFound().build();

        if (tourRegRepo.existsByUserIdAndMonumentId(userId, monumentId))
            return ResponseEntity.ok(Map.of("message", "Already registered"));

        TourRegistration tr = new TourRegistration(userOpt.get(), monOpt.get());
        tourRegRepo.save(tr);
        Monument m = tr.getMonument();
        return ResponseEntity.ok(TourRegistrationDto.builder()
                .id(m.getId()).name(m.getName()).city(m.getCity())
                .registeredAt(tr.getRegisteredAt().toString()).build());
    }

    @GetMapping("/tour-registrations/{userId}")
    public ResponseEntity<List<TourRegistrationDto>> getTourRegistrations(@PathVariable Long userId) {
        List<TourRegistrationDto> result = tourRegRepo.findByUserId(userId).stream()
                .map(t -> TourRegistrationDto.builder()
                        .id(t.getMonument().getId())
                        .name(t.getMonument().getName())
                        .city(t.getMonument().getCity())
                        .registeredAt(t.getRegisteredAt().toString())
                        .build())
                .toList();
        return ResponseEntity.ok(result);
    }

    // ── Monument Visits ───────────────────────────────────────────────────────

    @PostMapping("/monument-visit")
    public ResponseEntity<?> trackMonumentVisit(@RequestBody Map<String, Object> body) {
        Long userId     = Long.valueOf(body.get("userId").toString());
        Long monumentId = Long.valueOf(body.get("monumentId").toString());

        Optional<User>     userOpt = userRepo.findById(userId);
        Optional<Monument> monOpt  = monumentRepo.findById(monumentId);
        if (userOpt.isEmpty() || monOpt.isEmpty()) return ResponseEntity.notFound().build();

        if (monumentVisitRepo.existsByUserIdAndMonumentId(userId, monumentId))
            return ResponseEntity.ok(Map.of("message", "Already visited"));

        MonumentVisit mv = new MonumentVisit(userOpt.get(), monOpt.get());
        monumentVisitRepo.save(mv);
        Monument m = mv.getMonument();
        return ResponseEntity.ok(MonumentVisitDto.builder()
                .id(m.getId()).name(m.getName()).city(m.getCity())
                .visitedAt(mv.getVisitedAt().toString()).build());
    }

    @GetMapping("/monument-visits/{userId}")
    public ResponseEntity<List<MonumentVisitDto>> getMonumentVisits(@PathVariable Long userId) {
        List<MonumentVisitDto> result = monumentVisitRepo.findByUserId(userId).stream()
                .map(v -> MonumentVisitDto.builder()
                        .id(v.getMonument().getId())
                        .name(v.getMonument().getName())
                        .city(v.getMonument().getCity())
                        .visitedAt(v.getVisitedAt().toString())
                        .build())
                .toList();
        return ResponseEntity.ok(result);
    }

    // ── Full Activity Summary ─────────────────────────────────────────────────

    @GetMapping("/summary/{userId}")
    public ResponseEntity<ActivitySummaryDto> getSummary(@PathVariable Long userId) {
        if (!userRepo.existsById(userId)) return ResponseEntity.notFound().build();

        Map<String, Integer> pageVisits = new LinkedHashMap<>();
        pageVisitRepo.findByUserId(userId).forEach(v -> pageVisits.put(v.getPage(), v.getVisitCount()));

        List<TourRegistrationDto> tours = tourRegRepo.findByUserId(userId).stream()
                .map(t -> TourRegistrationDto.builder()
                        .id(t.getMonument().getId()).name(t.getMonument().getName())
                        .city(t.getMonument().getCity()).registeredAt(t.getRegisteredAt().toString())
                        .build())
                .toList();

        List<MonumentVisitDto> monuments = monumentVisitRepo.findByUserId(userId).stream()
                .map(v -> MonumentVisitDto.builder()
                        .id(v.getMonument().getId()).name(v.getMonument().getName())
                        .city(v.getMonument().getCity()).visitedAt(v.getVisitedAt().toString())
                        .build())
                .toList();

        return ResponseEntity.ok(ActivitySummaryDto.builder()
                .pageVisits(pageVisits)
                .toursRegistered(tours)
                .monumentsVisited(monuments)
                .build());
    }
}
