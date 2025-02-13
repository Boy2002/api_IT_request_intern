package com.rider.it_request_service.mapper;

import com.rider.it_request_service.dto.CategoryDTO;
import com.rider.it_request_service.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}
