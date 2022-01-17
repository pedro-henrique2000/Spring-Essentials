package com.projects.essentials.client;

import com.projects.essentials.domain.Anime;
import com.projects.essentials.requests.AnimePostRequestBody;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseHandler());
        restTemplate.getInterceptors().add(new RequestLoggingInterceptor());
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("pedro", "123"));

        ResponseEntity<Anime> entity = restTemplate.getForEntity("http://localhost:8080/animes/13", Anime.class);
        log.info(entity);

        Anime object = restTemplate.getForObject("http://localhost:8080/animes/13", Anime.class);
        log.info(object);

        Anime[] animes = restTemplate.getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));

        ResponseEntity<List<Anime>> animesList = restTemplate.exchange("http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        });
        log.info(animesList.getBody());

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBody.builder().name("Fullmetal").build();

        Anime anime =
                restTemplate.postForObject("http://localhost:8080/animes/", animePostRequestBody, Anime.class);

        log.info("Post {}", anime);

        ResponseEntity<Anime> responseEntity = restTemplate
                .exchange("http://localhost:8080/animes/", HttpMethod.POST,
                        new HttpEntity<>(animePostRequestBody, createJsonHeader()),
                        Anime.class);

        log.info("Post with response entity {}", responseEntity.getBody());

        AnimePostRequestBody animeToUpdate = AnimePostRequestBody.builder().name("Update").build();

        ResponseEntity<Void> responseEntityPut = restTemplate
                .exchange("http://localhost:8080/animes/{id}", HttpMethod.PUT,
                        new HttpEntity<>(animeToUpdate, createJsonHeader()),
                        Void.class, "17");

        log.info("Put with response entity {}", responseEntityPut.getBody());

        String idToDelete = "18";

        ResponseEntity<Void> responseEntityDelete = restTemplate
                .exchange("http://localhost:8080/animes/{id}", HttpMethod.DELETE,
                        new HttpEntity<>(animeToUpdate, createJsonHeader()),
                        Void.class, idToDelete);

        log.info("Deleted with id {} {}", idToDelete, responseEntityDelete.getBody());
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }
}
