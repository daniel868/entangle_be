package org.example.entablebe.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.generic.GenericSuccessResponse;
import org.example.entablebe.pojo.medical.DiseaseDto;
import org.example.entablebe.pojo.medical.DiseaseRequestDto;
import org.example.entablebe.pojo.medical.MedicalNoteDto;
import org.example.entablebe.pojo.medical.TreatmentItemDto;
import org.example.entablebe.service.MedicalService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medical")
public class MedicalController {

    private final MedicalService medicalService;

    public MedicalController(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    @RequestMapping(value = "/diseaseTreatments", method = RequestMethod.GET, produces = {"application/json"})
    public Object diseaseTreatments(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Pageable pageable,
                                    @RequestParam(value = "searchString", required = false) String searchString) {
        UserEntangle currentUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return medicalService.getAllDiseaseForUser(currentUser.getId(), pageable, searchString);
    }

    @RequestMapping(value = "/addTreatment/{diseaseId}", method = RequestMethod.POST)
    public Object addTreatmentToDisease(@PathVariable("diseaseId") Long diseaseId,
                                        @RequestBody List<TreatmentItemDto> treatmentItems) {

        UserEntangle currentUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean result = medicalService.addNewTreatment(diseaseId, currentUser.getId(), treatmentItems);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return new GenericSuccessResponse<>(response);
    }

    @RequestMapping(value = "/disease", method = RequestMethod.POST)
    public Object addNewDisease(@RequestBody DiseaseRequestDto diseaseRequestDto) {
        UserEntangle currentUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean result = medicalService.addNewDisease(currentUser.getId(), diseaseRequestDto);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return new GenericSuccessResponse<>(response);
    }


    @RequestMapping(value = "/disease/{diseaseId}", method = RequestMethod.DELETE)
    public Object deleteDisease(@PathVariable("diseaseId") Long diseaseId) {

        boolean result = medicalService.removeDisease(diseaseId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return new GenericSuccessResponse<>(response);
    }

    @RequestMapping(value = "/treatmentItems", method = RequestMethod.GET)
    public Object getTreatmentItems(@RequestParam("searchString") String searchString) {
        List<TreatmentItemDto> response = medicalService.getTreatmentItem(searchString);
        return new GenericSuccessResponse<>(response);
    }

    @RequestMapping(value = "/modifyTreatment/{treatmentId}", method = RequestMethod.PUT)
    public Object updateTreatment(@RequestBody List<TreatmentItemDto> itemDtoList,
                                  @PathVariable("treatmentId") Long treatmentId) {
        boolean response = medicalService.updateTreatmentForDisease(treatmentId, itemDtoList);
        return new GenericSuccessResponse<>(response);
    }

    @RequestMapping(path = "/medical-note/{diseaseId}", method = RequestMethod.POST)
    public GenericSuccessResponse<MedicalNoteDto> addNewMedicalNote(@RequestBody MedicalNoteDto medicalNoteDto,
                                                                    @PathVariable("diseaseId") Long diseaseId) {
        MedicalNoteDto response = medicalService.addNewNote(medicalNoteDto, diseaseId);
        return new GenericSuccessResponse<>(response);
    }

    @RequestMapping(path = "/medical-note/{diseaseId}", method = RequestMethod.GET)
    public GenericSuccessResponse<List<MedicalNoteDto>> getMedicalNoteForDisease(@PathVariable("diseaseId") Long diseaseId) {
        List<MedicalNoteDto> response = medicalService.getAllNotesForDisease(diseaseId);
        return new GenericSuccessResponse<>(response);
    }
}
