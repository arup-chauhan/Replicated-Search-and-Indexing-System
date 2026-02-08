package com.engine.metadata;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.PageRequest;

import com.engine.metadata.MetadataServiceGrpc;
import com.engine.metadata.MetadataRequest;
import com.engine.metadata.MetadataResponse;
import com.engine.metadata.MetadataListRequest;
import com.engine.metadata.MetadataListResponse;

import java.util.Comparator;
import java.util.stream.Collectors;

@GrpcService
public class MetadataServiceImpl extends MetadataServiceGrpc.MetadataServiceImplBase {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 200;

    private final MetadataService metadataService;

    public MetadataServiceImpl(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Override
    public void getMetadata(MetadataRequest request, StreamObserver<MetadataResponse> responseObserver) {
        final long docId;
        try {
            docId = Long.parseLong(request.getDocumentId());
        } catch (NumberFormatException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("documentId must be a numeric id")
                    .asRuntimeException());
            return;
        }

        metadataService.get(docId).ifPresentOrElse(
                doc -> {
                    responseObserver.onNext(toGrpc(doc));
                    responseObserver.onCompleted();
                },
                () -> responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Metadata not found for id: " + request.getDocumentId())
                        .asRuntimeException())
        );
    }

    @Override
    public void listMetadata(MetadataListRequest request, StreamObserver<MetadataListResponse> responseObserver) {
        int page = request.getPage() < 0 ? DEFAULT_PAGE : request.getPage();
        int size = request.getSize() <= 0 ? DEFAULT_SIZE : Math.min(request.getSize(), MAX_SIZE);

        var pageable = PageRequest.of(page, size);
        var pageData = metadataService.getAll().stream()
                .sorted(Comparator.comparing(DocumentMeta::getCreatedAt).reversed())
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .map(this::toGrpc)
                .collect(Collectors.toList());

        MetadataListResponse response = MetadataListResponse.newBuilder()
                .addAllMetadata(pageData)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private MetadataResponse toGrpc(DocumentMeta doc) {
        MetadataResponse.Builder builder = MetadataResponse.newBuilder()
                .setDocumentId(String.valueOf(doc.getId()))
                .setTitle(doc.getTitle() == null ? "" : doc.getTitle())
                .setAuthor("system")
                .setCreatedAt(doc.getCreatedAt() == null ? "" : doc.getCreatedAt().toString());

        if (doc.getTags() != null && !doc.getTags().isEmpty()) {
            String tags = doc.getTags().stream()
                    .map(Tag::getName)
                    .filter(name -> name != null && !name.isBlank())
                    .sorted()
                    .collect(Collectors.joining(","));
            if (!tags.isEmpty()) {
                builder.putExtraFields("tags", tags);
            }
        }
        return builder.build();
    }
}
