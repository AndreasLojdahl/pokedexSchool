package com.example.pokedex.controllers;

import com.example.pokedex.dto.PokemonDto;
import com.example.pokedex.services.PokemonConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonConsumerService pokemonConsumerService;

    @GetMapping
    public ResponseEntity<PokemonDto> findPokemons(@RequestParam String name){
        var pokemons = pokemonConsumerService.search(name);
        return ResponseEntity.ok(pokemons);
    }
}
