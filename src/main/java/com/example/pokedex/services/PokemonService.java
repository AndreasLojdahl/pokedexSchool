package com.example.pokedex.services;

import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.entities.PokemonName;
import com.example.pokedex.repositories.PokemonNameRepository;
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
    @Autowired
    private PokemonNameRepository pokemonNameRepository;

    @Cacheable(value = "pokedexCache", key ="#name")
    public List<Pokemon> findPokemonByName(String name){

        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        var pokemonNames = this.findPokemonNamesInDB(name);
        System.out.println(pokemonNames.size()+ "name SSIIZZE");
        if(pokemons.isEmpty() || pokemons.size() < pokemonNames.size()){




            List<Pokemon> finalPokemons = pokemons;
            pokemonNames.forEach(pokemonName -> {
                var pokemonDto = pokemonConsumerService.search(pokemonName.getName());
                var pokemon = new Pokemon(pokemonDto.getName(), pokemonDto.getHeight(),pokemonDto.getWeight());
                finalPokemons.add(this.save(pokemon));
            });
            return finalPokemons;
        }
        return pokemons;



//        if(pokemons.isEmpty()){
//            System.out.println("Fetching data...");
//            var pokemonDto = pokemonConsumerService.search(name);
//            var pokemon = new Pokemon(pokemonDto.getName(),pokemonDto.getHeight(),pokemonDto.getWeight());
//            pokemons.add(this.save(pokemon));
//        }
//        return pokemons;
    }

    public List<PokemonName> findPokemonNamesInDB(String name){
        var pokemonNames = pokemonNameRepository.findAll();
        pokemonNames = pokemonNames.stream().filter(pokemonName -> pokemonName.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return pokemonNames;
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
