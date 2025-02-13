package com.rider.it_request_service.mapper;


import com.rider.it_request_service.dto.CategoryDTO;
import com.rider.it_request_service.dto.RequestDTO;
import com.rider.it_request_service.entity.Category;
import com.rider.it_request_service.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}
