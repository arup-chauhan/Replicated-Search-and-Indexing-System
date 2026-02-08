package com.engine.api;

import com.engine.grpc.Hit;
import com.engine.query.LuceneQueryService;
import com.engine.suggest.SuggestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final LuceneQueryService luceneQueryService;
    private final SuggestService suggestService;

    public SearchController(LuceneQueryService luceneQueryService, SuggestService suggestService) {
        this.luceneQueryService = luceneQueryService;
        this.suggestService = suggestService;
    }

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String q,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "0") int offset) {
        try {
            List<Hit> hits = luceneQueryService.search(q, size, offset);

            // record query in Redis
            suggestService.recordQuery(q);

            List<Map<String, Object>> response = hits.stream().map(hit -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", hit.getId());
                map.put("title", hit.getTitle());
                map.put("score", hit.getScore());
                return map;
            }).toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
