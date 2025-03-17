package org.example.entablebe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entablebe.pojo.contact.ContactRequest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResilienceRateLimiterTest {

    @Autowired
    private MockMvc mockMvc;

    @RepeatedTest(20)
    public void whenCallNewContactInitialize_multipleTimes_shouldBlock(RepetitionInfo repetitionInfo) throws Exception {
        ResultMatcher result = repetitionInfo.getCurrentRepetition() <= 10 ? status().isOk() :
                status().isTooManyRequests();

        ContactRequest request = new ContactRequest();
        request.setPatientContactInfo("test");
        request.setPatientSituation("test");
        request.setPatientName("test");
        ObjectMapper objectMapper  = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/patient/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(result);
    }
}
