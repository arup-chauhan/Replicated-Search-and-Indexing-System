package com.engine.api;

import com.engine.dto.MetadataDto;
import com.engine.mapper.MetadataMapper;
import com.engine.metadata.DocumentMeta;
import com.engine.metadata.MetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {
    private final MetadataService svc;
    private final MetadataMapper metadataMapper;

    public MetadataController(MetadataService svc, MetadataMapper metadataMapper) {
        this.svc = svc;
        this.metadataMapper = metadataMapper;
    }

    // Create
    @PostMapping
    public ResponseEntity<MetadataDto> create(@RequestBody MetadataDto dto) {
        DocumentMeta entity = metadataMapper.toEntity(dto);
        DocumentMeta saved = svc.save(entity);
        return ResponseEntity.ok(metadataMapper.toDto(saved));
    }

    // Get one
    @GetMapping("/{id}")
    public ResponseEntity<MetadataDto> get(@PathVariable Long id) {
        return svc.get(id)
                .map(m -> ResponseEntity.ok(metadataMapper.toDto(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<MetadataDto>> getAll() {
        return ResponseEntity.ok(
                svc.getAll().stream()
                        .map(metadataMapper::toDto)
                        .toList()
        );
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return svc.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
