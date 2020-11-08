package com.example.pokedex.services;

import com.example.pokedex.entities.Ability;
import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.entities.PokemonName;
import com.example.pokedex.entities.Type;
import com.example.pokedex.repositories.PokemonNameRepository;
import com.example.pokedex.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;
    @Autowired
    private PokemonNameRepository pokemonNameRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Pokemon> findPokemon(String name, Integer minHeight, Integer maxHeight, Integer minWeight, Integer maxWeight, String ability, String type) {

        Query query = new Query();

        if(name != null && !name.isEmpty()) {
            this.fetchPokemonFromPokeApi(name);
            query.addCriteria(Criteria.where("name").regex(name.toLowerCase()));
        }
        if(maxHeight != null || minHeight != null){
            query.addCriteria(getCriteriaForQueryFromParams("height", minHeight, maxHeight));
        }
        if(maxWeight != null || minWeight != null){
            query.addCriteria(getCriteriaForQueryFromParams("weight", minWeight, maxWeight));
        }
        if(ability != null){
            checkIfValueIsTooShort(ability);
            query.addCriteria(Criteria.where("abilities.name").regex(ability.toLowerCase()));
        }
        if(type != null){
            checkIfValueIsTooShort(type);
            query.addCriteria(Criteria.where("types.name").regex(type.toLowerCase()));
        }

         return mongoTemplate.find(query, Pokemon.class);

    }

    private Criteria getCriteriaForQueryFromParams(String param, Integer min, Integer max ) {

        if (max != null && min != null) {
            return Criteria.where(param).gte(min).lte(max);
        }else if( max != null ){
            return Criteria.where(param).lte(max);
        }else {
            return Criteria.where(param).gte(min);
        }
    }

    private void fetchPokemonFromPokeApi(String name) {

        checkIfValueIsTooShort(name);

        var pokemonList = this.filterPokemonByNameInDB(name);
        var pokemonNames = this.findPokemonNamesInDB(name);

        if (pokemonList.size() < pokemonNames.size()) {

            pokemonNames.forEach(pokemonName -> {
                var pokemonAlreadyExist = pokemonRepository.findByName(pokemonName.getName());
                if (pokemonAlreadyExist.isEmpty()) {
                    var pokemonDto = pokemonConsumerService.search(pokemonName.getName());

                    ArrayList<Ability> abilityList = new ArrayList<>();
                    pokemonDto.getAbilities().forEach(ability -> abilityList.add(ability.getAbility()));

                    ArrayList<Type> typeList = new ArrayList<>();
                    pokemonDto.getTypes().forEach(type -> typeList.add(type.getType()));

                    var pokemon = new Pokemon(pokemonDto.getName(), pokemonDto.getHeight(), pokemonDto.getWeight(), abilityList, typeList);
                    this.save(pokemon);
                }
            });
        }
    }

    public void checkIfValueIsTooShort(String value){
        if (value.toCharArray().length < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("value %s is too short", value));
        }
    }

    public List<PokemonName> findPokemonNamesInDB(String name) {
        var pokemonNames = pokemonNameRepository.findAll();
        pokemonNames = pokemonNames.stream().filter(pokemonName -> pokemonName.getName()
                .toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return pokemonNames;
    }

    public List<Pokemon> filterPokemonByNameInDB(String name) {
        var pokemonList = pokemonRepository.findAll();
        pokemonList = pokemonList.stream().filter(pokemon -> pokemon.getName()
                .toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return pokemonList;
    }

    public Pokemon findPokemonById(String id) {
        return pokemonRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon"));
    }

    public void update(String id, Pokemon pokemon) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon");
        }
        pokemon.setId(id);
        pokemonRepository.save(pokemon);
    }

    public Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    public void delete(String id) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon");
        }
        pokemonRepository.deleteById(id);
    }

}
