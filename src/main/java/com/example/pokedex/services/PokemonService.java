package com.example.pokedex.services;

import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;

    public List<Pokemon> findPokemonByName(String name){
        var pokemons = pokemonRepository.findAll();

        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        if(pokemons.isEmpty()){
            var pokemonDto = pokemonConsumerService.search(name);
            var pokemon = new Pokemon(pokemonDto.getName(),pokemonDto.getHeight(),pokemonDto.getWeight());
            pokemons.add(this.save(pokemon));
        }
        return pokemons;
    }

    public Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

}
