package org.example.entablebe.repository;

import org.example.entablebe.entity.TreatmentItem;

import java.util.List;

public interface MedicalRepository {
    List<TreatmentItem> fetchMatchTreatmentItems(String searchString);
}
