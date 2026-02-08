package com.engine.mapper;

import com.engine.dto.TagDto;
import com.engine.metadata.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag entity);
    Tag toEntity(TagDto dto);
    List<TagDto> toDtoList(List<Tag> entities);
}
