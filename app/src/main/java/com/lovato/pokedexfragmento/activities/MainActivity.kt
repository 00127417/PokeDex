package com.lovato.pokedexfragmento.activities


import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.lovato.pokedexfragmento.AppConstants
import com.lovato.pokedexfragmento.R
import com.lovato.pokedexfragmento.adapters.PokemonAdapter
import com.lovato.pokedexfragmento.fragments.MainContentFragment
import com.lovato.pokedexfragmento.fragments.MainListFragment
import com.lovato.pokedexfragmento.models.Pokemon
import com.lovato.pokedexfragmento.utilities.NetworkUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pokemon_list_fragment.*
import kotlinx.android.synthetic.main.pokemon_list_fragment.view.*
import kotlinx.android.synthetic.main.viewer_element_pokemon.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity(), MainListFragment.SearchNewPokemonListener {

    private lateinit var mainFragment: MainListFragment
    private lateinit var mainContentFragment: MainContentFragment

    private var pokeList = ArrayList<Pokemon>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pokeList = savedInstanceState?.getParcelableArrayList(AppConstants.dataset_saveinstance_key) ?: ArrayList()

        initMainFragment()


    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.dataset_saveinstance_key, pokeList)
        super.onSaveInstanceState(outState)
        println("---------------------------------")
        println(pokeList)
        println("---------------------------------")
    }

    fun initMainFragment() {
        mainFragment = MainListFragment.newInstance(pokeList)

        val resource = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            R.id.main_fragment
        else {
            mainContentFragment = MainContentFragment.newInstance(Pokemon())
            changeFragment(R.id.land_main_cont_fragment, mainContentFragment)

            R.id.land_main_fragment
        }

        changeFragment(resource, mainFragment)


    }


    fun addPokemonToList(pokemon: Pokemon) {
        pokeList.add(pokemon)
        mainFragment.updatePokemonAdapter(pokeList)
        println(pokeList)
        //Limpiar
        Log.d("Number", pokeList.size.toString())


    }


    override fun managePortraitItemClick(pokemon: Pokemon) {
        startActivity(Intent(this, PokemonViewer::class.java).putExtra("CLAVIER", pokemon.url))
    }

    private fun changeFragment(id: Int, frag: Fragment) {
        supportFragmentManager.beginTransaction().replace(id, frag).commit()
    }

    override fun manageLandscapeItemClick(pokemon: Pokemon) {
        mainContentFragment = MainContentFragment.newInstance(pokemon)
        changeFragment(R.id.land_main_cont_fragment, mainContentFragment)
    }


    override fun searchPokemon(pokeId: String) {
        searchbar.setText("")
        QueryPokemonTask().execute(pokeId)

    }



    private inner class QueryPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg query: String): String {

            if (query.isNullOrEmpty()) return ""

            val ID = query[0]
            val pokeAPI = NetworkUtils().buildUrl("type", ID)

            return try {
                NetworkUtils().getResponseFromHttpUrl(pokeAPI)
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }

        }

        override fun onPostExecute(pokemonInfo: String) {
            pokeList.clear()
            if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val results = root.getJSONArray("pokemon")
                MutableList(20) { i ->
                    val resulty = JSONObject(results[i].toString())
                    val result = JSONObject(resulty.getString("pokemon"))

                    addPokemonToList(
                        Pokemon(
                            i,
                            result.getString("name").capitalize(),
                            R.string.n_a_value.toString(),
                            R.string.n_a_value.toString(),
                            R.string.n_a_value.toString(),
                            R.string.n_a_value.toString(),
                            result.getString("url"),
                            R.string.n_a_value.toString()
                        )
                    )


                }
            } else {
                MutableList(20) { i ->
                    Pokemon(
                        i,
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString()
                    )
                }
            }

        }
    }
}
