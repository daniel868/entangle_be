package org.example.entablebe.pojo.userInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserInfoResponse {
    private String username;
    private String email;
    private List<String> qualification;
    private List<String> competences;
}
