package com.heritage.controller;

import com.heritage.dto.*;
import com.heritage.model.*;
import com.heritage.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository             userRepo;
    private final PageVisitRepository        pageVisitRepo;
    private final TourRegistrationRepository tourRegRepo;
    private final MonumentVisitRepository    monumentVisitRepo;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (User u : userRepo.findAll()) {
            Map<String, Object> row = buildUserRow(u, false);
            result.add(row);
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(u -> ResponseEntity.ok(buildUserRow(u, true)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return userRepo.findById(id).map(u -> {
            u.setRole(body.get("role"));
            userRepo.save(u);
            return ResponseEntity.ok(UserDto.builder()
                    .id(u.getId()).name(u.getName())
                    .email(u.getEmail()).role(u.getRole()).build());
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepo.existsById(id)) return ResponseEntity.notFound().build();
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private Map<String, Object> buildUserRow(User u, boolean fullDetail) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id",    u.getId());
        row.put("name",  u.getName());
        row.put("email", u.getEmail());
        row.put("role",  u.getRole());

        Map<String, Integer> pageVisits = new LinkedHashMap<>();
        pageVisitRepo.findByUserId(u.getId())
                .forEach(v -> pageVisits.put(v.getPage(), v.getVisitCount()));
        row.put("pageVisits", pageVisits);

        if (fullDetail) {
            // Full lists for detail view
            List<TourRegistrationDto> tours = tourRegRepo.findByUserId(u.getId()).stream()
                    .map(t -> TourRegistrationDto.builder()
                            .id(t.getMonument().getId())
                            .name(t.getMonument().getName())
                            .city(t.getMonument().getCity())
                            .registeredAt(t.getRegisteredAt().toString())
                            .build())
                    .toList();
            row.put("toursRegistered", tours);

            List<MonumentVisitDto> visits = monumentVisitRepo.findByUserId(u.getId()).stream()
                    .map(v -> MonumentVisitDto.builder()
                            .id(v.getMonument().getId())
                            .name(v.getMonument().getName())
                            .city(v.getMonument().getCity())
                            .visitedAt(v.getVisitedAt().toString())
                            .build())
                    .toList();
            row.put("monumentsVisited", visits);
        } else {
            // Just counts for table view
            row.put("toursRegistered",  tourRegRepo.findByUserId(u.getId()).size());
            row.put("monumentsVisited", monumentVisitRepo.findByUserId(u.getId()).size());
        }
        return row;
    }
}
