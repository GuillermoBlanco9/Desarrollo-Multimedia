package com.gba.pokedexgrupo1;

import com.gba.pokedexgrupo1.PokemonFetchResults;


import retrofit2.Call;
import retrofit2.http.GET;

public interface PokemonAPIService {
    @GET("pokemon/?limit=150")
    Call<PokemonFetchResults> getPokemons();
}
