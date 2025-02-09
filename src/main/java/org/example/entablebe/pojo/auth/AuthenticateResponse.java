package org.example.entablebe.pojo.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AuthenticateResponse {
    private String token;
    private String username;
    private Long expiresInSecond;
    private boolean accountActivate;
    private String userEmail;
}
