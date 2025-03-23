package org.example.entablebe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entablebe.pojo.auth.LoginRequest;
import org.example.entablebe.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResilienceCircuitBreakerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach()
    public void init() {
        when(authService.loginUser(any(LoginRequest.class))).thenThrow(new RuntimeException("Exception thrown at login"));
    }

    @RepeatedTest(10)
    public void whenLoginFailed_circuitBreakerShouldHandleIt(RepetitionInfo repetitionInfo) throws Exception {
        ResultMatcher result = repetitionInfo.getCurrentRepetition() <= 5 ? status().is5xxServerError() : status().isForbidden();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("test");
        loginRequest.setUsername("test");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(result);

    }
}
