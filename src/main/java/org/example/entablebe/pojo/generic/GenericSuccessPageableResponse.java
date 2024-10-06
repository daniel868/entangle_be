package org.example.entablebe.pojo.generic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class GenericSuccessPageableResponse<T> {
    private List<T> payload;
    private int pageSize;
    private int currentPage;
    private int nextPage;
    private int totalCount;
}
