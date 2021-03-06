package com.example.pokedex.controllers;

import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.services.PokemonConsumerService;
import com.example.pokedex.services.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;

    @GetMapping
    @Operation(summary = "Find a pokemon or get all pokemon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the pokemon/pokemon",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content)
    })
    public ResponseEntity<List<Pokemon>> findPokemon(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minHeight,
            @RequestParam(required = false) Integer maxHeight,
            @RequestParam(required = false) Integer minWeight,
            @RequestParam(required = false) Integer maxWeight,
            @RequestParam(required = false) String ability,
            @RequestParam(required = false) String type){
        var pokemons = pokemonService.findPokemon(name,minHeight,maxHeight,minWeight,maxWeight, ability, type);
        return ResponseEntity.ok(pokemons);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a specific pokemon.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the pokemon.",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class)) }),
            @ApiResponse(responseCode = "404", description = "Couldn't find pokemon.", content = @Content)
    })
    public ResponseEntity<Pokemon> findPokemonById(@PathVariable String id){
        return ResponseEntity.ok(pokemonService.findPokemonById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Creates a pokemon.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created a pokemon.",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Admin authentication is required", content = @Content),
    })
    public ResponseEntity<Pokemon> savePokemon(@Validated @RequestBody Pokemon pokemon){
        var savePokemon = pokemonService.save(pokemon);
        var uri = URI.create("/api/v1/pokemon/" + savePokemon.getId());
        return ResponseEntity.created(uri).body(savePokemon);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Updates a pokémon.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated pokemon."),
            @ApiResponse(responseCode = "401", description = "Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Admin authentication is required", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request body.", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePokemon(@PathVariable String id,@Validated @RequestBody Pokemon pokemon){
        pokemonService.update(id, pokemon);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Deletes a pokemon.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted pokemon."),
            @ApiResponse(responseCode = "401", description = "Authentication is required.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Admin authentication is required", content = @Content),
            @ApiResponse(responseCode = "404", description = "Couldn't find pokemon.", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePokemon(@PathVariable String id){
        pokemonService.delete(id);
    }

    // ** collects all pokemonNames to DB **

//    @GetMapping("/test")
//    public void harvestPokemons(){
//        System.out.println("I harvest");
//        pokemonConsumerService.getAllPokes();
//    }
}