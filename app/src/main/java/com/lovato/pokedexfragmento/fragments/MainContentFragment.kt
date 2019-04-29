package com.lovato.pokedexfragmento.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovato.pokedexfragmento.R
import com.lovato.pokedexfragmento.models.Pokemon
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_content_fragment_layout.*


class MainContentFragment: Fragment(){

    var pokemon = Pokemon()

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

        bindData(view)

        return view
    }

    fun bindData(view: View){
        Picasso.with(view.context)
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

}