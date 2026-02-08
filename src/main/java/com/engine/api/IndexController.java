package com.engine.api;

import com.engine.dto.DocumentMetaDto;
import com.engine.indexing.LuceneIndexingService;
import com.engine.mapper.DocumentMetaMapper;
import com.engine.metadata.DocumentMeta;
import com.engine.metadata.MetadataService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/index")
public class IndexController {

    private record IndexReq(@NotBlank String title,
                            @NotBlank String content,
                            List<String> tags) {}

    private final LuceneIndexingService luceneIndexingService;
    private final MetadataService metadata;
    private final DocumentMetaMapper mapper;

    public IndexController(LuceneIndexingService luceneIndexingService, MetadataService metadata, DocumentMetaMapper mapper) {
        this.luceneIndexingService = luceneIndexingService;
        this.metadata = metadata;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> index(@Valid @RequestBody IndexReq req) {
        DocumentMeta m = metadata.saveWithTags(req.title(), req.content(), req.tags());
        try {
            luceneIndexingService.indexDoc(String.valueOf(m.getId()), req.title(), req.content(), req.tags());
            return ResponseEntity.ok(mapper.toDto(m));
        } catch (Exception e) {
            // Best-effort compensation to reduce DB/index divergence on partial failure.
            metadata.delete(m.getId());
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Indexing failed; metadata write rolled back",
                    "details", e.getMessage()
            ));
        }
    }
}
