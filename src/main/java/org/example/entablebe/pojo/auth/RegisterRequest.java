package org.example.entablebe.pojo.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RegisterRequest extends LoginRequest {
    private String email;
    private List<String> qualification;
    private List<String> competences;
}
