package com.jaruizes.pocs.springai.jobsmatcher.api.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto.PositionDTO;
import com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto.QueryDTO;
import com.jaruizes.pocs.springai.jobsmatcher.api.rest.dto.SkillDTO;
import com.jaruizes.pocs.springai.jobsmatcher.api.rest.mappers.PositionMapper;
import com.jaruizes.pocs.springai.jobsmatcher.service.JobsMatcherService;
import com.jaruizes.pocs.springai.jobsmatcher.service.model.Position;
import com.jaruizes.pocs.springai.jobsmatcher.service.model.Skill;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class JobsMatcherRestController {

    private JobsMatcherService jobsMatcherService;

    public JobsMatcherRestController(JobsMatcherService jobsMatcherService) {
        this.jobsMatcherService = jobsMatcherService;
    }

    /**
     * Este endpoint se encarga de recibir un fichero pdf correspondiente a un curriculum y lo almacena en el sistema.
     * Lo almacena en una base de datos vectorial
     * Devuelve OK (200) o KO (4xx/5xx) en función de si se ha podido almacenar correctamente o no.
     *
     * @param file Fichero pdf correspondiente a un curriculum.
     * @param name Nombre del candidato
     *
     * @return OK (200) o KO (4xx/5xx)
     */
    @PostMapping(path = "/curriculums", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> uploadCurriculum(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws IOException {
        if(file.isEmpty()) {
            return new ResponseEntity<>("El archivo está vacío", HttpStatus.BAD_REQUEST);
        }

        // Almacenar el curriculum en la base de datos vectorial
       this.jobsMatcherService.storeCurriculum(file.getBytes(), name);

       return new ResponseEntity<>("Curriculum almacenado correctamente", HttpStatus.OK);

    }

    @PostMapping(path = "/queries", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getCVs(@RequestBody QueryDTO query) {
        return new ResponseEntity<>(this.jobsMatcherService.getCVs(query.getSkills(), query.getThreshold()), HttpStatus.OK);
    }

    @PostMapping(path = "/positions", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> createPosition(@RequestBody PositionDTO positionDTO) {
        var position = PositionMapper.INSTANCE.toPosition(positionDTO);
        return new ResponseEntity<>(this.jobsMatcherService.createPosition(position), HttpStatus.OK);
    }

}
