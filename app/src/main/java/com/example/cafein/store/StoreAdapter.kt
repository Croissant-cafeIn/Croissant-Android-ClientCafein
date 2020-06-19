package com.example.cafein.store

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafein.R
import com.example.cafein.databinding.ItemStoreBinding

class StoreAdapter(
    private val items: List<Store>,
    private val clickListener: (store: Store) -> Unit) :
    RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {
    class StoreViewHolder(val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root){
        var storeImage: ImageView? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_store, parent, false)
        val viewHolder = StoreViewHolder(ItemStoreBinding.bind(view))
        view.setOnClickListener{
            clickListener.invoke(items[viewHolder.adapterPosition])
        }
        var congestBtn = viewHolder.itemView.findViewById<TextView>(R.id.congestionButton)

        congestBtn.addTextChangedListener {
            if (congestBtn.text.toString() == "여유" || congestBtn.text.toString() == "0")
                congestBtn.setTextColor(Color.GREEN)
            if (congestBtn.text.toString() == "보통" || congestBtn.text.toString() == "1")
                congestBtn.setTextColor(Color.parseColor("#FFA500"))
            if (congestBtn.text.toString() == "혼잡" || congestBtn.text.toString() == "2")
                congestBtn.setTextColor(Color.RED)
        }

        viewHolder.storeImage = viewHolder.itemView.findViewById<ImageView>(R.id.store_image)

        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.binding.store = items[position]

        Glide.with(holder.itemView.context).load(items[position].storeUrl).into(holder.storeImage!!)

    }

}