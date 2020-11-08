package com.example.pokedex.entities;

import org.springframework.data.annotation.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

public class Pokemon implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @NotBlank
    private String name;
    @Min(1)
    private int height;
    @Min(1)
    private int weight;
    private List<Ability> abilities;
    private List<Type> types;

    public Pokemon(){

    }

    public Pokemon(String name, int height, int weight, List<Ability> abilities, List<Type> types) {
        this.name = name.toLowerCase();
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
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

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }
}
