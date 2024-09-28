package org.example.entablebe.pojo.generic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenericErrorResponse {
    private String errorMessage;
    private int errorCode;
}
