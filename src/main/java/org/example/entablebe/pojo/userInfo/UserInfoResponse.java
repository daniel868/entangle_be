package org.example.entablebe.pojo.userInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserInfoResponse {
    private List<String> qualification;
    private List<String> competences;
}
