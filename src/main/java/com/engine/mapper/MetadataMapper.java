package com.engine.mapper;

import com.engine.dto.MetadataDto;
import com.engine.metadata.DocumentMeta;
import com.engine.metadata.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MetadataMapper {

    @Mapping(target = "tags", source = "tags")
    MetadataDto toDto(DocumentMeta entity);

    @Mapping(target = "tags", source = "tags")
    DocumentMeta toEntity(MetadataDto dto);

    // Custom conversion methods for tags
    default String mapTagToString(Tag tag) {
        return tag != null ? tag.getName() : null;
    }

    default Tag mapStringToTag(String name) {
        if (name == null) return null;
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }

    // Handle list conversions
    default List<String> mapTagsToStrings(List<Tag> tags) {
        return tags != null
                ? tags.stream().map(this::mapTagToString).collect(Collectors.toList())
                : null;
    }

    default List<Tag> mapStringsToTags(List<String> names) {
        return names != null
                ? names.stream().map(this::mapStringToTag).collect(Collectors.toList())
                : null;
    }
}
