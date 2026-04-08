package com.heritage.controller;

import com.heritage.model.Monument;
import com.heritage.repository.MonumentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monuments")
public class MonumentController {

    private final MonumentRepository repo;

    public MonumentController(MonumentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Monument> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monument> getById(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Monument create(@RequestBody Monument monument) {
        return repo.save(monument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Monument> update(@PathVariable Long id, @RequestBody Monument updated) {
        return repo.findById(id).map(m -> {
            m.setName(updated.getName()); m.setCity(updated.getCity());
            m.setState(updated.getState()); m.setEra(updated.getEra());
            m.setYear(updated.getYear()); m.setCategory(updated.getCategory());
            m.setDescription(updated.getDescription());
            m.setImage(updated.getImage()); m.setThumbnail(updated.getThumbnail());
            return ResponseEntity.ok(repo.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
