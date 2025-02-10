package com.rider.it_request.mapper;

import com.rider.it_request.dto.CategoryDTO;
import com.rider.it_request.dto.RequestDTO;
import com.rider.it_request.entity.Category;
import com.rider.it_request.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}
