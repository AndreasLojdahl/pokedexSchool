package com.example.pokedex.services;

import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;

    @Cacheable(value = "pokedexCache", key ="#name")
    public List<Pokemon> findPokemonByName(String name){
        var pokemons = pokemonRepository.findAll();

        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        if(pokemons.isEmpty()){
            System.out.println("Fetching data...");
            var pokemonDto = pokemonConsumerService.search(name);
            var pokemon = new Pokemon(pokemonDto.getName(),pokemonDto.getHeight(),pokemonDto.getWeight());
            pokemons.add(this.save(pokemon));
        }
        return pokemons;
    }

    public Pokemon findPokemonById(String id){
        return pokemonRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon"));
    }

    @CachePut(value = "pokedexCache", key = "id")
    public void update(String id, Pokemon pokemon){
        if(!pokemonRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon");
        }
        pokemon.setId(id);
        pokemonRepository.save(pokemon);
    }

    @CachePut(value = "pokedexCache", key = "#result.id")
    public Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    @CacheEvict(value = "pokedexCache", allEntries = true)
    public void delete(String id){
        if(!pokemonRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon");
        }
        pokemonRepository.deleteById(id);

    }

}
