package com.projects.essentials.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AnimePostRequestBody {

    @NotBlank(message = "name is invalid")
    @Schema(description = "Anime's Name", example = "Naruto")
    private String name;
}
