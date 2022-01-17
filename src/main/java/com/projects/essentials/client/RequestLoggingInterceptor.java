package com.projects.essentials.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {


        String bodyString = new String(body, StandardCharsets.UTF_8);
        log.info("==========Request");
        log.info("Request Body -> {}", bodyString);
        log.info("Headers -> {}", request.getHeaders());

        ClientHttpResponse execute = execution.execute(request, body);

        return execute;
    }
}
