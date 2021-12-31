package com.projects.essentials.controller;

import com.projects.essentials.domain.Anime;
import com.projects.essentials.service.AnimeService;
import com.projects.essentials.util.AnimeCreator;
import com.projects.essentials.util.AnimePostRequestBodyCreator;
import com.projects.essentials.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    @Mock
    private DateUtil dateUtil;

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage =
                new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        when(animeService.listAll(any()))
                .thenReturn(animePage);

        when(animeService.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        when(animeService.findByIdOrThrowNotFoundException(anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        doNothing().when(animeService).update(any(), anyLong());
    }

    @Test
    void list_ReturnListOfAnimeInsideAPageObject_whenSuccessful() {
        Anime validAnime = AnimeCreator.createValidAnime();
        Page<Anime> body = animeController.list(any()).getBody();

        assertNotNull(body);
        assertTrue(body.getContent().contains(validAnime));
    }

    @Test
    void list_ReturnListOfAnime_whenSuccessful() {
        Anime validAnime = AnimeCreator.createValidAnime();
        List<Anime> animes = animeController.listAll().getBody();

        assertNotNull(animes);
        assertTrue(animes.contains(validAnime));
    }

    @Test
    void findById_ReturnAnimeWithExpectedId_whenSuccessful() {
        String name = AnimeCreator.createValidAnime().getName();

        ResponseEntity<Anime> anime = animeController.listById(1L);

        assertNotNull(anime.getBody());
        assertEquals(1L, anime.getBody().getId());
        assertEquals(name, anime.getBody().getName());

    }

    @Test
    void findByName_ReturnAnimeWithExpectedName_whenSuccessful() {

        when(animeService.findByName(anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        String name = AnimeCreator.createValidAnime().getName();

        ResponseEntity<List<Anime>> anime = animeController.findByName(name);

        assertNotNull(anime.getBody());
        assertEquals(name, anime.getBody().get(0).getName());
        assertEquals(1, anime.getBody().size());
    }

    @Test
    void findByName_ReturnEmptyListOfAnime_whenNotFound() {
        when(animeService.findByName(anyString()))
                .thenReturn(List.of());

        ResponseEntity<List<Anime>> anime = animeController.findByName(anyString());

        assertNotNull(anime.getBody());
        assertEquals(0, anime.getBody().size());
    }

    @Test
    void save_SaveAndReturnaAnAnime_whenSuccesful() {
        when(animeService.save(any()))
                .thenReturn(AnimeCreator.createValidAnime());

        ResponseEntity<Anime> anime =
                animeController.saveAnime(AnimePostRequestBodyCreator.createValidAnime());

        assertNotNull(anime.getBody());
        assertNotNull(anime.getBody().getId());
        assertEquals(AnimeCreator.createValidAnime().getId(), anime.getBody().getId());
    }

    @Test
    void update_UpdateAnAnime_whenSuccessful() {
        ResponseEntity<Void> responseEntity =
                animeController.replace(AnimePostRequestBodyCreator.createValidAnime(), anyLong());

        verify(animeService, times(1)).update(any(), anyLong());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void delete_DeleteAnAnime_whenSuccesful() {
        ResponseEntity<Void> responseEntity =
                animeController.delete(anyLong());

        verify(animeService, times(1)).delete(anyLong());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT.value(), responseEntity.getStatusCodeValue());
    }


}