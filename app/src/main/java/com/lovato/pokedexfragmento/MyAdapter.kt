package com.lovato.pokedexfragmento

import com.lovato.pokedexfragmento.models.Pokemon

object AppConstants{
    val dataset_saveinstance_key = "CLE"
    val MAIN_LIST_KEY = "key_list_movies"
}

interface MyPokeAdapter {
    fun changeDataSet(newDataSet : List<Pokemon>)
}