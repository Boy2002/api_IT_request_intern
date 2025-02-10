package com.rider.it_request.mapper;

import com.rider.it_request.dto.RequestDTO;
import com.rider.it_request.dto.RequestFileDTO;
import com.rider.it_request.entity.Request;
import com.rider.it_request.entity.RequestFile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestFileMapper {

    RequestFileMapper INSTANCE = Mappers.getMapper(RequestFileMapper.class);

    RequestFileDTO toDTO(RequestFile requestFile);

    RequestFile toEntity(RequestFileDTO requestFileDTO);
}
