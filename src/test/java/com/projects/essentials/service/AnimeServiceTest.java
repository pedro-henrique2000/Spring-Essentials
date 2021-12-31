package com.projects.essentials.service;

import com.projects.essentials.domain.Anime;
import com.projects.essentials.exceptions.BadRequestException;
import com.projects.essentials.repository.AnimeRepository;
import com.projects.essentials.util.AnimeCreator;
import com.projects.essentials.util.AnimePostRequestBodyCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    @BeforeEach
    void setup() {


//        when(animeRepository.save(any())).thenReturn(AnimeCreator.createValidAnime());

    }

    @Test
    void listAllNoPageable_ReturnListOfAnime_whenSuccessful() {

        when(animeRepository.findAll()).thenReturn(List.of(AnimeCreator.createValidAnime()));


        Anime validAnime = AnimeCreator.createValidAnime();
        List<Anime> animeList = animeService.listAllNonPageable();

        assertNotNull(animeList);
        assertTrue(animeList.contains(validAnime));
    }

    @Test
    void listAll_ReturnListOfAnimeInPage_whenSuccessful() {

        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));


        when(animeRepository.findAll(any(PageRequest.class))).thenReturn(animePage);

        Anime validAnime = AnimeCreator.createValidAnime();
        Page<Anime> page = animeService.listAll(PageRequest.of(1, 1));

        assertNotNull(animePage);
        assertTrue(page.getContent().contains(validAnime));
    }

    @Test
    void findById_ReturnAnimeWithExpectedId_whenSuccessful() {
        when(animeRepository.findById(anyLong())).thenReturn(Optional.of(AnimeCreator.createValidAnime()));


        String name = AnimeCreator.createValidAnime().getName();

        Anime anime = animeService.findByIdOrThrowNotFoundException(1L);

        assertNotNull(anime);
        assertEquals(1L, anime.getId());
        assertEquals(name, anime.getName());

    }

    @Test
    void findByName_ReturnAnimeWithExpectedName_whenSuccessful() {

        when(animeRepository.findByName(anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        String name = AnimeCreator.createValidAnime().getName();

        List<Anime> byName = animeService.findByName(name);

        assertNotNull(byName);
        assertEquals(name, byName.get(0).getName());
        assertEquals(1, byName.size());
    }

    @Test
    void findByName_ReturnEmptyListOfAnime_whenNotFound() {
        when(animeRepository.findByName(anyString()))
                .thenReturn(List.of());

        List<Anime> anime = animeService.findByName("");

        assertNotNull(anime);
        assertEquals(0, anime.size());
    }

    @Test
    void save_SaveAndReturnaAnAnime_whenSuccesful() {
        when(animeRepository.save(any()))
                .thenReturn(AnimeCreator.createValidAnime());

        Anime anime =
                animeService.save(AnimePostRequestBodyCreator.createValidAnime());

        assertNotNull(anime);
        assertNotNull(anime.getId());
        assertEquals(AnimeCreator.createValidAnime().getId(), anime.getId());
    }

    @Test
    void update_UpdateAnAnime_whenSuccessful() {

        when(animeRepository.findById(anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        animeService.update(AnimePostRequestBodyCreator.createValidAnime(), anyLong());

        verify(animeRepository, times(1)).save(any());

    }

    @Test
    void update_ThrowBaDRequest_whenNotFoundId() {

        when(animeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            animeService.update(AnimePostRequestBodyCreator.createValidAnime(), 1L);
        });

        verify(animeRepository, never()).save(any());

    }

    @Test
    void delete_DeleteAnAnime_whenSuccesful() {

        when(animeRepository.findById(anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        animeService.delete(anyLong());

        verify(animeRepository, times(1)).delete(any());
    }


}