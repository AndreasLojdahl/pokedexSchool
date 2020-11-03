package com.example.pokedex.dto;

import java.util.List;

public class HarvestPokemonResultsDto {

    private List<PokemonNameDto> results;

    public HarvestPokemonResultsDto() {
    }

    public HarvestPokemonResultsDto(List<PokemonNameDto> results) {
        this.results = results;
    }

    public List<PokemonNameDto> getResults() {
        return results;
    }

    public void setResults(List<PokemonNameDto> results) {
        this.results = results;
    }
}
