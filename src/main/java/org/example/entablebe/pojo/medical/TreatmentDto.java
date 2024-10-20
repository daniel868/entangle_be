package org.example.entablebe.pojo.medical;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TreatmentDto {
    private String specialistName;
    private List<TreatmentItemDto> items;
}
