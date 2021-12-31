package com.projects.essentials.util;

import com.projects.essentials.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
    public static AnimePostRequestBody createAnimeToBeSaved() {
        return AnimePostRequestBody.builder().name("Naruto Shippuden").build();
    }

    public static AnimePostRequestBody createValidAnime() {
        return AnimePostRequestBody.builder().name("Naruto Shippuden").build();
    }

    public static AnimePostRequestBody createInvalidUpdatedAnime() {
        return AnimePostRequestBody.builder().name("INVALID").build();
    }
}
