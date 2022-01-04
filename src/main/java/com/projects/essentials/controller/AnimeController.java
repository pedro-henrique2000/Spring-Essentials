package com.projects.essentials.controller;

import com.projects.essentials.domain.Anime;
import com.projects.essentials.requests.AnimePostRequestBody;
import com.projects.essentials.service.AnimeService;
import com.projects.essentials.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/animes")
@Log4j2
public class AnimeController {

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private AnimeService animeService;

    @Tag(name = "animes")
    @GetMapping
    @Operation(summary = "List all animes paginated", description = "The default page size is 20")
    public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @Tag(name = "animes")
    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        return ResponseEntity.ok(animeService.listAllNonPageable());
    }

    @Tag(name = "animes")
    @GetMapping("/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(name = "name") String name) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        return ResponseEntity.ok(animeService.findByName(name));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Anime> listById(@PathVariable Long id) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        return ResponseEntity.ok(animeService.findByIdOrThrowNotFoundException(id));
    }

    @GetMapping("by-id/{id}")
    public ResponseEntity<Anime> listById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        log.info(userDetails);

        return ResponseEntity.ok(animeService.findByIdOrThrowNotFoundException(id));
    }

    @PostMapping()
    public ResponseEntity<Anime> saveAnime(@RequestBody @Valid AnimePostRequestBody anime) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(animeService.save(anime));
    }

    @DeleteMapping("admin/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Not Found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        animeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> replace(@RequestBody AnimePostRequestBody anime, @PathVariable Long id) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        animeService.update(anime, id);

        return ResponseEntity.noContent().build();
    }

}