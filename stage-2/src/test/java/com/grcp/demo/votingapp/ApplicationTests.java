package com.grcp.demo.votingapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApplicationTests {

    private static final String HTTP_LOCALHOST_BASE = "http://localhost:%s";

    @Value(value="${local.server.port}")
    protected Integer port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String baseUrl() {
            return HTTP_LOCALHOST_BASE.formatted(port);
    }
}
