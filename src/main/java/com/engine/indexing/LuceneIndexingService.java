package com.engine.indexing;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class LuceneIndexingService {
    private final IndexManager indexManager;
    private final Counter indexedDocsCounter;

    public LuceneIndexingService(IndexManager indexManager) {
        this.indexManager = indexManager;
        this.indexedDocsCounter = Counter.builder("engine_indexed_documents_total")
                .description("Total number of documents indexed")
                .register(Metrics.globalRegistry);
    }

    public void indexDoc(String id, String title, String content, Collection<String> tags) throws IOException {
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        if (tags != null) {
            tags.forEach(t -> doc.add(new StringField("tag", t, Field.Store.YES)));
        }

        indexManager.writer().updateDocument(new Term("id", id), doc);
        indexManager.writer().commit();
        indexedDocsCounter.increment();
    }
}
