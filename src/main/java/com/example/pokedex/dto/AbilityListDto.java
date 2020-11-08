package com.example.pokedex.dto;

import com.example.pokedex.entities.Ability;

public class AbilityListDto {

    private Ability ability;

    public AbilityListDto(){

    }

    public AbilityListDto(Ability ability) {
        this.ability = ability;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }
}
