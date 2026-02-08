package com.engine.query;

import com.engine.grpc.Hit;
import com.engine.indexing.IndexManager;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LuceneQueryService {
    private final IndexManager indexManager;
    private final QueryBuilder queryBuilder;

    public LuceneQueryService(IndexManager indexManager, QueryBuilder queryBuilder) {
        this.indexManager = indexManager;
        this.queryBuilder = queryBuilder;
    }

    public List<Hit> search(String q, int size, int offset) throws Exception {
        try (DirectoryReader reader = DirectoryReader.open(indexManager.writer())) {
            IndexSearcher searcher = new IndexSearcher(reader);
            Query query = queryBuilder.build(q, "content");
            TopDocs top = searcher.search(query, offset + size);
            ScoreDoc[] hits = top.scoreDocs;

            List<Hit> out = new ArrayList<>();
            for (int i = offset; i < Math.min(hits.length, offset + size); i++) {
                org.apache.lucene.document.Document d = searcher.doc(hits[i].doc);
                out.add(Hit.newBuilder()
                        .setId(d.get("id"))
                        .setTitle(d.get("title"))
                        .setScore(hits[i].score)
                        .build());
            }
            return out;
        }
    }
}
