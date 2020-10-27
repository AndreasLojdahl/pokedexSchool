package com.example.pokedex.entities;

import org.springframework.data.annotation.Id;

public class PokemonName {

    @Id
    private String id;
    private String name;


    public PokemonName() {
    }

    public PokemonName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
