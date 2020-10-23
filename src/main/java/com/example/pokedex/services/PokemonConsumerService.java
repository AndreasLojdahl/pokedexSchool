package com.example.pokedex.services;

import com.example.pokedex.dto.PokemonDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@ConfigurationProperties(value = "example.pokemon", ignoreUnknownFields = false)
public class PokemonConsumerService {

    private final RestTemplate restTemplate;

    @Value("example.pokemon.url")
    private String url;

    public PokemonConsumerService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    public PokemonDto search(String name){

        var urlWithNameQuery = url + name;
        var pokemon = restTemplate.getForObject(urlWithNameQuery, PokemonDto.class);

        if(pokemon == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No pokemon found");
        }
        return pokemon;
    }

    public List<PokemonDto> findAll(String name){
        return null;
    }

    public void setUrl(String url){
        this.url = url;
    }
}
