package org.example.entablebe.pojo.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateResponse {
    private String token;
    private String username;
    private Long expiresInSecond;
}
