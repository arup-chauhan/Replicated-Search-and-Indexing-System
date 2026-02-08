package com.engine.indexing;

import com.engine.config.EngineProps;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class IndexManager implements AutoCloseable {
    private final Directory dir;
    private final IndexWriter writer;

    public IndexManager(EngineProps props, Analyzer analyzer) throws IOException {
        Path path = Path.of(props.getIndexPath());
        this.dir = FSDirectory.open(path);
        this.writer = new IndexWriter(dir, new IndexWriterConfig(analyzer));
    }

    public IndexWriter writer() {
        return writer;
    }

    @Override
    public void close() throws IOException {
        writer.close();
        dir.close();
    }
}
