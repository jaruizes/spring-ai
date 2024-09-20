package com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CurriculumDTO implements Serializable {

    public static final long serialVersionUID = 1L;
    private String file;
    private String name;

}
