package com.lovato.pokedexfragmento.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovato.pokedexfragmento.MyPokeAdapter
import com.lovato.pokedexfragmento.R
import com.lovato.pokedexfragmento.adapters.PokemonAdapter
import com.lovato.pokedexfragmento.adapters.PokemonSimpleListAdapter
import com.lovato.pokedexfragmento.models.Pokemon
import kotlinx.android.synthetic.main.pokemon_list_fragment.*
import kotlinx.android.synthetic.main.pokemon_list_fragment.view.*

class MainListFragment: Fragment(){

    var listenerTool :  SearchNewMovieListener? = null
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var pokemons: ArrayList<Pokemon>
    private lateinit var pokemonsAdapter: MyPokeAdapter

    companion object {
        fun newInstance(dataset : ArrayList<Pokemon>): MainListFragment{
            val newFragment = MainListFragment()
            newFragment.pokemons = dataset
            return newFragment
        }
    }


    interface SearchNewMovieListener{
        fun searchPokemon(pokeId: String)

        fun managePortraitItemClick(pokemon: Pokemon)

        fun manageLandscapeItemClick(pokemon: Pokemon)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.pokemon_list_fragment, container, false)

        initRecyclerView(resources.configuration.orientation, view)
        initSearchButton(view)

        return view
    }

    fun initRecyclerView(orientation:Int, container:View){
        val linearLayoutManager = LinearLayoutManager(this.context)

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            pokemonsAdapter = PokemonAdapter(pokemons, {movie:Pokemon->listenerTool?.managePortraitItemClick(movie)})
            container.rv_pokemon_list.adapter = pokemonsAdapter as PokemonAdapter
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            pokemonsAdapter = PokemonSimpleListAdapter (pokemons, { movie:Pokemon->listenerTool?.manageLandscapeItemClick(movie)})
            container.rv_pokemon_list.adapter = pokemonsAdapter as PokemonSimpleListAdapter
        }

        container.rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    fun initSearchButton(container:View) = container.searchbarbutton.setOnClickListener {
        listenerTool?.searchPokemon(searchbar.text.toString())
    }



    fun updateMoviesAdapter(movieList: ArrayList<Pokemon>){ pokemonsAdapter.changeDataSet(movieList) }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchNewMovieListener) {
            listenerTool = context
        } else {
            throw RuntimeException("Se necesita una implementación de  la interfaz")
        }
    }

}