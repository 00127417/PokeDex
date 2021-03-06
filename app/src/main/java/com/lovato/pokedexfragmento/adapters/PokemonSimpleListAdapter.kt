package com.lovato.pokedexfragmento.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovato.pokedexfragmento.MyPokeAdapter
import com.lovato.pokedexfragmento.R
import com.lovato.pokedexfragmento.models.Pokemon
import kotlinx.android.synthetic.main.list_item_pokemon.view.*

class PokemonSimpleListAdapter(var items: List<Pokemon>, val clickListener: (Pokemon) -> Unit): RecyclerView.Adapter<PokemonSimpleListAdapter.ViewHolder>(), MyPokeAdapter {
    override fun changeDataSet(newDataSet: List<Pokemon>) {
        this.items =newDataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], clickListener)

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: Pokemon, clickListener: (Pokemon) -> Unit) = with(itemView) {
            tv_pokemon_id_ip.text = item.id.toString()
            tv_pokemon_name_ip.text = item.name
            this.setOnClickListener { clickListener(item) }
        }
    }
}