package com.example.pokedex.dto;

import java.util.List;

public class PokemonDto {

    private String name;
    private int height;
    private int weight;
    private List<AbilityListDto> abilities;
    private List<TypeListDto> types;

    public PokemonDto(){

    }

    public PokemonDto(String name, int height, int weight, List<AbilityListDto> abilities, List<TypeListDto> types) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<AbilityListDto> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<AbilityListDto> abilities) {
        this.abilities = abilities;
    }

    public List<TypeListDto> getTypes() {
        return types;
    }

    public void setTypes(List<TypeListDto> types) {
        this.types = types;
    }
}
