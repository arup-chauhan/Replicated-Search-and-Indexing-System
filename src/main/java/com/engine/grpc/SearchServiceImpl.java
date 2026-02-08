package com.engine.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import com.engine.query.LuceneQueryService;
import com.engine.suggest.SuggestService;

import java.util.List;

@GrpcService
public class SearchServiceImpl extends SearchServiceGrpc.SearchServiceImplBase {

    private final LuceneQueryService luceneQueryService;
    private final SuggestService suggestService;

    public SearchServiceImpl(LuceneQueryService luceneQueryService, SuggestService suggestService) {
        this.luceneQueryService = luceneQueryService;
        this.suggestService = suggestService;
    }

    @Override
    public void search(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        try {
            // 🔎 perform Lucene search
            List<Hit> hits = luceneQueryService.search(request.getQ(), request.getSize(), request.getOffset());

            // 📌 record the query in Redis
            suggestService.recordQuery(request.getQ());

            SearchResponse response = SearchResponse.newBuilder()
                    .addAllHits(hits)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
