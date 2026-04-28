package com.heritage.controller;

import com.heritage.dto.MonumentDto;
import com.heritage.model.Monument;
import com.heritage.repository.MonumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/monuments")
@RequiredArgsConstructor
public class MonumentController {

    private final MonumentRepository repo;

    @GetMapping
    public List<MonumentDto> getAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonumentDto> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(m -> ResponseEntity.ok(toDto(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MonumentDto> create(@RequestBody MonumentDto dto) {
        Monument m = toEntity(dto);
        return ResponseEntity.ok(toDto(repo.save(m)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonumentDto> update(@PathVariable Long id, @RequestBody MonumentDto dto) {
        return repo.findById(id).map(m -> {
            m.setName(dto.getName());         m.setCity(dto.getCity());
            m.setState(dto.getState());       m.setEra(dto.getEra());
            m.setYear(dto.getYear());         m.setCategory(dto.getCategory());
            m.setDescription(dto.getDescription());
            m.setImage(dto.getImage());       m.setThumbnail(dto.getThumbnail());
            m.setFacts(listToCsv(dto.getFacts()));
            m.setTourPoints(listToCsv(dto.getTourPoints()));
            return ResponseEntity.ok(toDto(repo.save(m)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Mapping helpers ───────────────────────────────────────────────────────

    private MonumentDto toDto(Monument m) {
        return MonumentDto.builder()
                .id(m.getId())
                .name(m.getName())
                .city(m.getCity())
                .state(m.getState())
                .era(m.getEra())
                .year(m.getYear())
                .category(m.getCategory())
                .description(m.getDescription())
                .image(m.getImage())
                .thumbnail(m.getThumbnail())
                .facts(csvToList(m.getFacts()))
                .tourPoints(csvToList(m.getTourPoints()))
                .build();
    }

    private Monument toEntity(MonumentDto dto) {
        Monument m = new Monument();
        m.setName(dto.getName());         m.setCity(dto.getCity());
        m.setState(dto.getState());       m.setEra(dto.getEra());
        m.setYear(dto.getYear());         m.setCategory(dto.getCategory());
        m.setDescription(dto.getDescription());
        m.setImage(dto.getImage());       m.setThumbnail(dto.getThumbnail());
        m.setFacts(listToCsv(dto.getFacts()));
        m.setTourPoints(listToCsv(dto.getTourPoints()));
        return m;
    }

    private List<String> csvToList(String csv) {
        if (csv == null || csv.isBlank()) return Collections.emptyList();
        return Arrays.stream(csv.split("\\|")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    private String listToCsv(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join("|", list);
    }
}
