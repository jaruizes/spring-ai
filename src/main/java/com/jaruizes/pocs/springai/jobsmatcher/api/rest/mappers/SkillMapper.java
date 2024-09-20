package com.jaruizes.pocs.springai.jobsmatcher.api.rest.mappers;

import com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto.SkillDTO;
import com.jaruizes.pocs.springai.jobsmatcher.service.model.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SkillMapper {
    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    Skill DTOtoSkill(SkillDTO skillDTO);
    SkillDTO skillToDTO(Skill skill);
}
