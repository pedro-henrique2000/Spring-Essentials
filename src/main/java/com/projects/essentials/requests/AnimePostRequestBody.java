package com.projects.essentials.requests;

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
    private String name;


}
