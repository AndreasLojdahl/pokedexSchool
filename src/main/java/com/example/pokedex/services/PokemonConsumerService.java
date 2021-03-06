package com.example.pokedex.services;

import com.example.pokedex.dto.PokemonDto;
import com.example.pokedex.repositories.PokemonNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PokemonConsumerService {

    private final RestTemplate restTemplate;

    @Value("${example.pokemon.url}")
    private String url;

    @Autowired
    private PokemonNameRepository pokemonNameRepository;

    public PokemonConsumerService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    public PokemonDto search(String name){

        var urlWithNameQuery = url + "/" + name;
        var pokemon = restTemplate.getForObject(urlWithNameQuery, PokemonDto.class);

        if(pokemon == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No pokemon found");
        }
        return pokemon;
    }

// ** USED FOR GETTING ALL POKEMONNAMES IN DB **
//    public void getAllPokes(){
//
//        var resultsDto = restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon?limit=2000&offset=0", HarvestPokemonResultsDto.class);
//        resultsDto.getResults().forEach(res -> pokemonNameRepository.save(new PokemonName(res.getName())));
//    }

    public void setUrl(String url){
        this.url = url;
    }
}
