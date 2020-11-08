package com.example.pokedex.dto;

import com.example.pokedex.entities.Type;

public class TypeListDto {

    private Type type;

    public TypeListDto() {
    }

    public TypeListDto(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
