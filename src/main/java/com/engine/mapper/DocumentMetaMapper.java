package com.engine.mapper;

import com.engine.dto.DocumentMetaDto;
import com.engine.metadata.DocumentMeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface DocumentMetaMapper {
    @Mapping(source = "tags", target = "tags")
    DocumentMetaDto toDto(DocumentMeta entity);

    @Mapping(source = "tags", target = "tags")
    DocumentMeta toEntity(DocumentMetaDto dto);

    List<DocumentMetaDto> toDtoList(List<DocumentMeta> entities);
}
