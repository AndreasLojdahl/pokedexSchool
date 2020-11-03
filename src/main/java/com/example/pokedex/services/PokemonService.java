package com.example.pokedex.services;

import com.example.pokedex.entities.Pokemon;
import com.example.pokedex.entities.PokemonName;
import com.example.pokedex.repositories.PokemonNameRepository;
import com.example.pokedex.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
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

    //@Cacheable(value = "pokedexCache", key = "#name")
//    public List<Pokemon> findPokemons(Map<String, Object> params) {
//
//        if (!params.isEmpty()) {
//
//            this.fetchPokemonsFromPokeApi(params);
//
//            var query = this.getQueryFromParams(params);
//
//            List<Pokemon> pokemons = mongoTemplate.find(query, Pokemon.class);
//            return pokemons;
//
//        } else {
//            var pokemons = pokemonRepository.findAll();
//            return pokemons;
//        }
//
//    }
//
//    private Query getQueryFromParams(Map<String, Object> params) {
//
//        Query query = new Query();
//        params.forEach((key, value) -> {
//
//            if (key.equals("name") && value != "") {
//                query.addCriteria(Criteria.where(key).regex(value.toString()));
//            } else if (key.equals("minWeight") || key.equals("minHeight") && value != "") {
//
//                String keyStr = key.replace("min", "").toLowerCase();
//                query.addCriteria(Criteria.where(keyStr).gt(Integer.parseInt(value.toString())));
//
//            }else if( key.equals("maxWeight") || key.equals("maxHeight") && value != ""){
//                System.out.println(value + " VALUE");
//                String keyStr = key.replace("max", "").toLowerCase();
//                query.addCriteria(Criteria.where(keyStr).lt(Integer.parseInt(value.toString())));
//
//            }
//             else {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("key %s is not a valid parameter", key));
//            }
//
//        });
//        return query;
//    }
    public List<Pokemon> findPokemons(String name, Integer maxHeight, Integer minHeight, Integer maxWeight, Integer minWeight) {

            Query query = new Query();

            if(name != null && !name.isEmpty()) {
                this.fetchPokemonsFromPokeApi(name);
                query.addCriteria(Criteria.where("name").regex(name.toLowerCase()));
            }
            if(maxHeight != null || minHeight != null){
                query.addCriteria(getCriteriaForQueryFromParams("height", minHeight, maxHeight));
            }
            if(maxWeight != null || minWeight != null){
                query.addCriteria(getCriteriaForQueryFromParams("weight", minWeight, maxWeight));
            }

            List<Pokemon> pokemons = mongoTemplate.find(query, Pokemon.class);
            return pokemons;
    }

    private Criteria getCriteriaForQueryFromParams(String param, Integer min, Integer max ) {

           if (max != null && min != null) {
                return Criteria.where(param).gte(max).lte(min);
            }else if( max != null ){
                return Criteria.where(param).gte(max);
            }else {
               return Criteria.where(param).lte(min);
           }
    }

    private void fetchPokemonsFromPokeApi(String name) {

                if (name.toCharArray().length < 3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("value %s is too short", name));
                }
                var pokemons = this.filterPokemonsByNameInDB(name);
                var pokemonNames = this.findPokemonNamesInDB(name);

                if (pokemons.size() < pokemonNames.size()) {

                    pokemonNames.forEach(pokemonName -> {
                        var pokemonAlreadyExist = pokemonRepository.findByName(pokemonName.getName());
                        if (pokemonAlreadyExist.isEmpty()) {
                            var pokemonDto = pokemonConsumerService.search(pokemonName.getName());
                            var pokemon = new Pokemon(pokemonDto.getName(), pokemonDto.getHeight(), pokemonDto.getWeight());
                            this.save(pokemon);
                        }
                    });
                };
    }


    public List<PokemonName> findPokemonNamesInDB(String name) {
        var pokemonNames = pokemonNameRepository.findAll();
        pokemonNames = pokemonNames.stream().filter(pokemonName -> pokemonName.getName()
                .toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return pokemonNames;
    }

    public List<Pokemon> filterPokemonsByNameInDB(String name) {
        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream().filter(pokemon -> pokemon.getName()
                .toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return pokemons;
    }

    public Pokemon findPokemonById(String id) {
        return pokemonRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon"));
    }

    //@CachePut(value = "pokedexCache", key = "id")
    public void update(String id, Pokemon pokemon) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon");
        }
        pokemon.setId(id);
        pokemonRepository.save(pokemon);
    }

    //@CachePut(value = "pokedexCache", key = "#result.id")
    public Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    //@CacheEvict(value = "pokedexCache", allEntries = true)
    public void delete(String id) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find pokemon");
        }
        pokemonRepository.deleteById(id);
    }

}
