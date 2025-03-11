package org.example.entablebe.utils.assemblers;

import org.example.entablebe.entity.MedicalNote;
import org.example.entablebe.pojo.medical.MedicalNoteDto;
import org.springframework.stereotype.Component;

@Component
public class MedicalNotesAssembler {

    public MedicalNoteDto assemble(MedicalNote note) {
        return MedicalNoteDto.builder()
                .id(note.getId())
                .createdBy(note.getCreatedBy().getUsername())
                .createdDate(note.getCreatedDate())
                .noteContent(note.getNoteContent())
                .build();
    }

    public MedicalNote assemble(MedicalNoteDto dto) {
        MedicalNote entity = new MedicalNote();
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setNoteContent(dto.getNoteContent());
        return entity;
    }
}
