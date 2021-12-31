package com.projects.essentials.util;

import com.projects.essentials.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime.builder().name("Naruto Shippuden").build();
    }

    public static Anime createValidAnime() {
        return Anime.builder().name("Naruto Shippuden").id(1L).build();
    }

    public static Anime createInvalidUpdatedAnime() {
        return Anime.builder().name("INVALID").id(1L).build();
    }

}
