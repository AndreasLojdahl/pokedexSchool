package com.example.pokedex.repositories;

import com.example.pokedex.entities.User;
import jdk.dynalink.Operation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String name);
}
