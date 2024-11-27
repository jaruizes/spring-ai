package com.jaruizes.pocs.springai.jobsmatcher.service.model;

import java.util.List;

import lombok.Data;

@Data
public class Position {
    private String id;
    private String title;
    private String description;

    private List<Skill> mustHave;
    private List<Skill> niceToHave;
}
