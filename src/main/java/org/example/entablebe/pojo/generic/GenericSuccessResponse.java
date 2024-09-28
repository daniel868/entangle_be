package org.example.entablebe.pojo.generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenericSuccessResponse<T> {
    private T payload;
}
