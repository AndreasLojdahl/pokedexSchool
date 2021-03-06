package com.example.pokedex.repositories;

import com.example.pokedex.entities.PokemonName;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonNameRepository extends MongoRepository<PokemonName, String> {

    List<PokemonName> findByName(String name);
}