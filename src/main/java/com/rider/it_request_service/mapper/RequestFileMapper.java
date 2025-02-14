package com.rider.it_request_service.mapper;

import com.rider.it_request_service.dto.RequestFileDTO;
import com.rider.it_request_service.entity.RequestFile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestFileMapper {

    RequestFileMapper INSTANCE = Mappers.getMapper(RequestFileMapper.class);

    RequestFileDTO toDTO(RequestFile requestFile);

    RequestFile toEntity(RequestFileDTO requestFileDTO);
}
