package org.example.entablebe.pojo.medical;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class MedicalNoteDto {
    private Long id;
    private String noteContent;
    private Date createdDate;
    private String createdBy;
}
