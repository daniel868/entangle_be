package org.example.entablebe.pojo.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterRequest extends LoginRequest {
    private String email;

}
