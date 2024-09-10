package org.example.entablebe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class EntableBeApplicationTests {

    @Test
    void contextLoads() {
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 25);
        System.out.println(date);
    }

}
