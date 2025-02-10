package com.rider.it_request.mapper;

import com.rider.it_request.dto.RequestDTO;
import com.rider.it_request.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    RequestDTO toDTO(Request request);

    Request toEntity(RequestDTO requestDTO);
}
