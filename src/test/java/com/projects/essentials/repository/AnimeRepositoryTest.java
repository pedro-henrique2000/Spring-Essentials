package com.projects.essentials.repository;

import com.projects.essentials.domain.Anime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for anime repository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    void save_PersistAnime_whenSuccesful() {
        Anime anime = createAnime();

        Anime savedAnime = this.animeRepository.save(anime);

        assertEquals(anime.getName(), savedAnime.getName());
        assertNotNull(savedAnime);
        assertNotNull(savedAnime.getId());
    }

    @Test
    void update_UpdateAnime_whenSuccesful() {

        String newName = "Naruto Shippuden";

        Anime anime = createAnime();

        Anime savedAnime = this.animeRepository.save(anime);

        savedAnime.setName(newName);

        Anime updatedAnime = this.animeRepository.save(anime);

        assertEquals(newName, updatedAnime.getName());
        assertNotNull(updatedAnime);
        assertEquals(anime.getId(), updatedAnime.getId());
    }

    @Test
    void delete_DeleteAnime_whenSuccesful() {

        Anime anime = createAnime();

        Anime savedAnime = this.animeRepository.save(anime);

        this.animeRepository.deleteById(savedAnime.getId());

        assertTrue(this.animeRepository.findById(savedAnime.getId()).isEmpty());
    }

    @Test
    void findByName_returnAnime_whenSuccesful() {

        Anime anime = createAnime();

        this.animeRepository.save(anime);

        List<Anime> foundAnime = this.animeRepository.findByName(anime.getName());

        assertFalse(foundAnime.isEmpty());
        assertEquals(anime.getName(), foundAnime.get(0).getName());
    }

    @Test
    void findByName_NotReturnAnime_whenInvalidName() {
        List<Anime> foundAnime = this.animeRepository.findByName("Invalid");

        assertTrue(foundAnime.isEmpty());
    }

    @Test
    void save_ThrowConstraintViolationException_WhenNameIsEmpty() {
        Anime anime = new Anime();

        assertThrows(ConstraintViolationException.class, () -> {
            this.animeRepository.save(anime);
        });
    }

    private Anime createAnime() {
        return Anime.builder().name("Naruto").build();
    }

}