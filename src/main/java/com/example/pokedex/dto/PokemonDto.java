package com.example.pokedex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PokemonDto {

    private String name;
    private String height;
    private String weight;

    public PokemonDto(){

    }

    public PokemonDto(String name, String height, String weight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
