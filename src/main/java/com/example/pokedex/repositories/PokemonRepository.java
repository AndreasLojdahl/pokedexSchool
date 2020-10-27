package com.example.pokedex.repositories;

import com.example.pokedex.entities.Pokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends MongoRepository<Pokemon, String> {

    List<Pokemon> findByName(String name);
}
