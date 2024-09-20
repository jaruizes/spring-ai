package com.jaruizes.pocs.springai.jobsmatcher.api.rest.mappers;

import com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto.PositionDTO;
import com.jaruizes.pocs.springai.jobsmatcher.service.model.Position;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SkillMapper.class})
public interface PositionMapper {
    PositionMapper INSTANCE = Mappers.getMapper(PositionMapper.class);

    Position toPosition(PositionDTO positionDTO);
    PositionDTO toPositionDTO(Position position);

}
