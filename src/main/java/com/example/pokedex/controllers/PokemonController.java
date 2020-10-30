package com.example.pokedex.controllers;

import com.example.pokedex.dto.PokemonDto;
import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.services.PokemonConsumerService;
import com.example.pokedex.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;

    //collects all pokemonnames to DB

//    @GetMapping("/test")
//    public void harvestPokemons(){
//        System.out.println("I harvest");
//        pokemonConsumerService.getAllPokes();
//
//    }

    @GetMapping
    @Secured("ROLE_USER")
    public ResponseEntity<List<Pokemon>> findPokemons(@RequestParam Map<String, Object> params){
        var pokemons = pokemonService.findPokemons(params);
        return ResponseEntity.ok(pokemons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> findPokemonById(@PathVariable String id){
        return ResponseEntity.ok(pokemonService.findPokemonById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Pokemon> savePokemon(@RequestBody Pokemon pokemon){
        var savePokemon = pokemonService.save(pokemon);
        var uri = URI.create("/api/v1/pokemon/" + savePokemon.getId());
        return ResponseEntity.created(uri).body(savePokemon);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void updatePokemon(@PathVariable String id,@RequestBody Pokemon pokemon){
        pokemonService.update(id, pokemon);
    }


    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePokemon(@PathVariable String id){
        pokemonService.delete(id);
    }


}
