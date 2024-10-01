package org.example.entablebe.controller;

import org.example.entablebe.repository.DiseaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disease")
public class DiseasesController {

    private final DiseaseRepository diseaseRepository;

    public DiseasesController(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Object getDisease(Pageable pageable) {
        return diseaseRepository.findAll(pageable);
    }
}
