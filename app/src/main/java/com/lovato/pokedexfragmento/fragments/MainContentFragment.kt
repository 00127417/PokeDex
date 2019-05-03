package com.lovato.pokedexfragmento.fragments

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovato.pokedexfragmento.R
import com.lovato.pokedexfragmento.models.Pokemon
import com.lovato.pokedexfragmento.utilities.NetworkUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_content_fragment_layout.*
import kotlinx.android.synthetic.main.main_content_fragment_layout.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class MainContentFragment: Fragment(){

    var pokemon = Pokemon()

    private lateinit var pokemonHelper : Pokemon


    companion object {
        fun newInstance(pokemon: Pokemon): MainContentFragment{
            val newFragment = MainContentFragment()
            newFragment.pokemon = pokemon
            return newFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.main_content_fragment_layout, container, false)

        FetchPokemonTask().execute(pokemon.url)

        bindData(view)

        return view
    }

    fun bindData(view: View){
        Picasso.with(view.context)
            .load(pokemon.sprite)
            .resize((this.resources.displayMetrics.widthPixels / this.resources.displayMetrics.density).toInt(), 256)
            .centerCrop()
            .error(R.drawable.ic_pokemon_go)
            .into(view.image_main_content_fragment)
        view.title_cf.text = pokemon.name
        view.weight_cf.text = pokemon.weight
        view.height_cf.text = pokemon.height
        view.fstType_cf.text = pokemon.fsttype
        view.sndType_cf.text = pokemon.sndtype
    }



    fun init(pokemon: Pokemon){
        Picasso.with(this.context)
            .load(pokemon.sprite)
            .resize((this.resources.displayMetrics.widthPixels / this.resources.displayMetrics.density).toInt(), 256)
            .centerCrop()
            .error(R.drawable.ic_pokemon_go)
            .into(image_main_content_fragment)
        title_cf.text = pokemon.name
        weight_cf.text = pokemon.weight
        height_cf.text = pokemon.height
        fstType_cf.text = pokemon.fsttype
        sndType_cf.text = pokemon.sndtype
    }


    private inner class FetchPokemonTask : AsyncTask<String, Void, String>(){


        override fun doInBackground(vararg query: String): String {

            if (query.isNullOrEmpty() || query[0].equals("N/A")) return ""

            val url = query[0]
            val pokeAPI = Uri.parse(url).buildUpon().build()
            val finalurl = try {
                URL(pokeAPI.toString())
            } catch (e: MalformedURLException) {
                println(pokeAPI)
                URL("")

            }

            return try {
                NetworkUtils().getResponseFromHttpUrl(finalurl)
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }

        }

        override fun onPostExecute(pokemonInfo: String)  {
            pokemon = if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val sprites = root.getString("sprites")
                val types = root.getJSONArray("types")
                val fsttype = JSONObject(types[0].toString()).getString("type")
                val sndtype = try {
                    JSONObject(types[1].toString()).getString("type")
                } catch (e: JSONException) {
                    ""
                }

                Pokemon(
                    root.getInt("id"),
                    root.getString("name").capitalize(),
                    JSONObject(fsttype).getString("name").capitalize(),
                    if (sndtype.isEmpty()) " " else JSONObject(sndtype).getString("name").capitalize(),
                    root.getString("weight"), root.getString("height"), root.getString("location_area_encounters"),
                    JSONObject(sprites).getString("front_default")
                )

            } else {
                Pokemon(
                    0,
                    R.string.n_a_value.toString(),
                    R.string.n_a_value.toString(),
                    R.string.n_a_value.toString(),
                    R.string.n_a_value.toString(),
                    R.string.n_a_value.toString(),
                    R.string.n_a_value.toString(),
                    R.string.n_a_value.toString()
                )
            }

            init(pokemon)
        }

    }

}