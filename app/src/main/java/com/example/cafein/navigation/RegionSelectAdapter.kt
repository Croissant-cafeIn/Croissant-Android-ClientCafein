package com.example.cafein.navigation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.red
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.recyclerview.widget.RecyclerView
import com.example.cafein.R
import com.example.cafein.databinding.ItemCityBinding


class RegionSelectAdapter(val items: List<City>,
                          private val clickListener: (city: City) -> Unit) :
    RecyclerView.Adapter<RegionSelectAdapter.CityViewHolder>() {

    class CityViewHolder(val binding: ItemCityBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        val viewHolder = CityViewHolder(ItemCityBinding.bind(view))

        view.setOnClickListener{
            clickListener.invoke(items[viewHolder.adapterPosition])

        }
        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.binding.city = items[position]
    }

}

