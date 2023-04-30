package com.sparcs.teamf;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "/application.yml")
@SpringBootTest
class TestApplication {

    @Test
    void contextLoads() {
    }
}
