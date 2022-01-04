package com.projects.essentials.config;

import com.projects.essentials.domain.Anime;
import com.projects.essentials.domain.DevdojoUser;
import com.projects.essentials.repository.AnimeRepository;
import com.projects.essentials.repository.DevdojoUserRepository;
import com.projects.essentials.requests.AnimePostRequestBody;
import com.projects.essentials.util.AnimeCreator;
import com.projects.essentials.util.AnimePostRequestBodyCreator;
import com.projects.essentials.wrapper.PageableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateAdmin;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private DevdojoUserRepository devdojoUserRepository;

    private static final DevdojoUser USER = DevdojoUser.builder()
            .name("Henrique")
            .username("Henrique")
            .password("$2a$10$WREdXrdtrJAcGl1k16Z8h.uyzjgePN70IsW3rLFMU2W.mw9W8JV5q")
            .authorities("ROLE_USER")
            .build();

    private static final DevdojoUser ADMIN = DevdojoUser.builder()
            .name("Pedro")
            .username("Pedro")
            .password("$2a$10$WREdXrdtrJAcGl1k16Z8h.uyzjgePN70IsW3rLFMU2W.mw9W8JV5q")
            .authorities("ROLE_ADMIN,ROLE_USER")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("Henrique", "123");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("Pedro", "123");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    void list_ReturnListOfAnimeInsideAPageObject_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devdojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> exchange = testRestTemplate.exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
        }).getBody();

        assertNotNull(exchange);
        assertEquals(expectedName, exchange.getContent().get(0).getName());
        assertEquals(1, exchange.getNumberOfElements());
    }

    @Test
    void list_ReturnListOfAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devdojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();

        List<Anime> exchange = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        assertNotNull(exchange);
        assertEquals(expectedName, exchange.get(0).getName());
        assertEquals(1, exchange.size());
    }

    @Test
    void findById_ReturnAnimeWithExpectedId_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devdojoUserRepository.save(USER);

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        assertNotNull(anime);
        assertEquals(expectedId, anime.getId());
    }

    @Test
    void findByName_ReturnAnimeWithExpectedName_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devdojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();

        String url = "/animes/find?name=" + expectedName;

        List<Anime> body = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        assertNotNull(body);
        assertEquals(expectedName, body.get(0).getName());
        assertEquals(1, body.size());
    }

    @Test
    void findByName_ReturnEmptyListOfAnime_whenNotFound() {

        String expectedName = "NAME";

        devdojoUserRepository.save(USER);

        String url = "/animes/find?name=" + expectedName;

        List<Anime> body = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        assertNotNull(body);
        assertEquals(0, body.size());
    }

    @Test
    void save_SaveAndReturnaAnAnime_whenSuccesful() {

        AnimePostRequestBody validAnime = AnimePostRequestBodyCreator.createValidAnime();

        devdojoUserRepository.save(USER);

        String expectedName = validAnime.getName();

        ResponseEntity<Anime> anime =
                testRestTemplate.postForEntity("/animes", validAnime, Anime.class);

        assertNotNull(anime.getBody());
        assertNotNull(anime.getBody().getId());
        assertEquals(expectedName, anime.getBody().getName());
        assertEquals(HttpStatus.CREATED, anime.getStatusCode());
    }

    @Test
    void update_UpdateAnAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devdojoUserRepository.save(USER);

        String id = savedAnime.getId().toString();

        AnimePostRequestBody validAnime = AnimePostRequestBodyCreator.createValidAnime();
        String expectedName = validAnime.getName();

        ResponseEntity<Void> exchange = testRestTemplate.exchange("/animes/{id}", HttpMethod.PUT, new HttpEntity<>(validAnime), Void.class, id);
        Anime findAnime = testRestTemplate.exchange("/animes/{id}", HttpMethod.GET, null, Anime.class, id).getBody();

        assertNotNull(exchange);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assert findAnime != null;
        assertEquals(expectedName, findAnime.getName());
    }

    @Test
    void delete_DeleteAnAnime_whenSuccesful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createValidAnime());

        devdojoUserRepository.save(ADMIN);

        String id = savedAnime.getId().toString();

        ResponseEntity<Void> exchange = testRestTemplateAdmin.exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, id);

        assertNotNull(exchange);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
    }

    @Test
    void delete_Returns403_whenUserIsNotAdmin() {
        devdojoUserRepository.save(USER);

        ResponseEntity<Void> exchange = testRestTemplate
                .exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, "");

        assertNotNull(exchange);
        assertEquals(HttpStatus.FORBIDDEN, exchange.getStatusCode());
    }

}
