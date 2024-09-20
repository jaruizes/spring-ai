package com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto;

import java.util.List;

import com.jaruizes.pocs.springai.jobsmatcher.service.model.Skill;
import lombok.Data;

@Data
public class PositionDTO {
    private String id;
    private String title;
    private String description;

    private List<SkillDTO> mustHave;
    private List<SkillDTO> niceToHave;
}
