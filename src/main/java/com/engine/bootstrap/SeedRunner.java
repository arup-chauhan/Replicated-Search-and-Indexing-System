package com.engine.bootstrap;


import com.engine.indexing.LuceneIndexingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class SeedRunner implements CommandLineRunner {
    private final LuceneIndexingService luceneIndexingService;
    public SeedRunner(LuceneIndexingService luceneIndexingService) { this.luceneIndexingService = luceneIndexingService; }
    @Override public void run(String... args) throws Exception {
        if (System.getenv().getOrDefault("ENGINE_SEED", "false").equalsIgnoreCase("true")) {
            luceneIndexingService.indexDoc("hello", "Hello World", "This is a seeded doc", java.util.List.of("seed"));
        }
    }
}
