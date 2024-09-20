package com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class QueryDTO {
    private List<String> skills;
    private Double threshold;
}
