package com.example.pokedex.controllers;

import com.example.pokedex.dto.PokemonDto;
import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.services.PokemonConsumerService;
import com.example.pokedex.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;

//    @GetMapping("/test")
//    public void harvestPokemons(){
//        System.out.println("I harvest");
//        pokemonConsumerService.getAllPokes();
//
//    }

    @GetMapping
    public ResponseEntity<List<Pokemon>> findPokemons(@RequestParam Map<String, Object> params){
        var pokemons = pokemonService.findPokemons(params);
        return ResponseEntity.ok(pokemons);
    }
}
