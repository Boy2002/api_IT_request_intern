package com.rider.it_request_service.mapper;

import com.rider.it_request_service.dto.RequestDTO;
import com.rider.it_request_service.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    RequestDTO toDTO(Request request);

    Request toEntity(RequestDTO requestDTO);
}
