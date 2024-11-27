package com.jaruizes.pocs.springai.jobsmatcher.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.jaruizes.pocs.springai.jobsmatcher.service.model.Position;

public interface JobsMatcherService {

    /**
     * Este método se encarga de almacenar un curriculum en la base de datos vectorial.
     *
     * @param file Fichero pdf correspondiente a un curriculum.
     * @param name Nombre del candidato
     */
    void storeCurriculum(byte[] file, String name);
    public String createPosition(Position position);

    String getCVs(List<String> skills, double threshold);
}
