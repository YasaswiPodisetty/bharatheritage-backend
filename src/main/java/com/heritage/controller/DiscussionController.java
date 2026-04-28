package com.heritage.controller;

import com.heritage.dto.DiscussionDto;
import com.heritage.model.Discussion;
import com.heritage.repository.DiscussionRepository;
import com.heritage.repository.MonumentRepository;
import com.heritage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionRepository discussionRepo;
    private final UserRepository       userRepo;
    private final MonumentRepository   monumentRepo;

    @GetMapping("/monument/{monumentId}")
    public ResponseEntity<List<DiscussionDto>> getByMonument(@PathVariable Long monumentId) {
        List<DiscussionDto> result = discussionRepo
                .findByMonumentIdOrderByTimestampAsc(monumentId)
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> body) {
        Long   userId     = Long.valueOf(body.get("userId").toString());
        Long   monumentId = Long.valueOf(body.get("monumentId").toString());
        String text       = body.get("text").toString().trim();

        if (text.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "Comment cannot be empty"));

        var userOpt = userRepo.findById(userId);
        var monOpt  = monumentRepo.findById(monumentId);
        if (userOpt.isEmpty() || monOpt.isEmpty()) return ResponseEntity.notFound().build();

        Discussion d = new Discussion(userOpt.get(), monOpt.get(), text);
        discussionRepo.save(d);
        return ResponseEntity.ok(toDto(d));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean isAdmin) {

        return discussionRepo.findById(id).map(d -> {
            if (!isAdmin && !d.getUser().getId().equals(userId))
                return ResponseEntity.status(403).<Void>build();
            discussionRepo.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private DiscussionDto toDto(Discussion d) {
        return DiscussionDto.builder()
                .id(d.getId())
                .userId(d.getUser().getId())
                .userName(d.getUser().getName())
                .monumentId(d.getMonument().getId())
                .text(d.getText())
                .timestamp(d.getTimestamp().toString())
                .build();
    }
}
