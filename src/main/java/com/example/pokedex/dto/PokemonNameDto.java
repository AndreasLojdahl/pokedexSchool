package com.example.pokedex.dto;

public class PokemonNameDto {

    private String name;

    public PokemonNameDto() {
    }

    public PokemonNameDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
