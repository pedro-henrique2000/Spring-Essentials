package com.projects.essentials.service;

import com.projects.essentials.domain.Anime;
import com.projects.essentials.exceptions.BadRequestException;
import com.projects.essentials.repository.AnimeRepository;
import com.projects.essentials.requests.AnimePostRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;

    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }

    public Page<Anime> listAll(Pageable pageable) {
        Page<Anime> page = animeRepository.findAll(pageable);
        return page;
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowNotFoundException(long id) {
        log.info("Searching by id: {}", id);

        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }


    @Transactional(rollbackFor = Exception.class)
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        Anime anime = Anime.builder()
                .name(animePostRequestBody.getName())
                .build();

        return animeRepository.save(anime);
    }

    public void delete(Long id) {
        animeRepository.delete(findByIdOrThrowNotFoundException(id));
    }

    public void update(AnimePostRequestBody animePostRequestBody, Long id) {
        Anime saved = findByIdOrThrowNotFoundException(id);

        Anime anime = Anime.builder()
                .id(saved.getId())
                .name(animePostRequestBody.getName())
                .build();

        animeRepository.save(anime);
    }
}
