package com.engine.query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.springframework.stereotype.Component;

@Component
public class QueryBuilder {
    private final Analyzer analyzer;

    public QueryBuilder(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public Query build(String q, String defaultField) throws Exception {
        return new QueryParser(defaultField, analyzer).parse(q);
    }
}
