package com.projects.essentials.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class RestTemplateResponseHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR)
            return true;

        if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
            return true;

        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            log.info("401 Unauthorized...");
            log.info("{} -> status", response.getStatusCode());
        }

        if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
            log.info("500 - Internal Server Error");
            log.info("{} -> status", response.getStatusCode());
        }

    }
}
