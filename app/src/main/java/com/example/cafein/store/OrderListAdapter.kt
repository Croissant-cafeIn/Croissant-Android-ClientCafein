package com.example.cafein.store

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafein.R
import com.example.cafein.databinding.ItemOrderlistBinding
import com.example.cafein.databinding.ItemStoreBinding

class OrderListAdapter(
    private val items: List<OrderList>,
    private val clickListener: (orderlist: OrderList) -> Unit) :
    RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>() {
    class OrderListViewHolder(val binding: ItemOrderlistBinding): RecyclerView.ViewHolder(binding.root){
        //var storeImage: ImageView? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orderlist, parent, false)
        val viewHolder = OrderListViewHolder(ItemOrderlistBinding.bind(view))
        view.setOnClickListener{
            clickListener.invoke(items[viewHolder.adapterPosition])
        }
        var ordercheckBtn = viewHolder.itemView.findViewById<TextView>(R.id.orderCheckButton)

        ordercheckBtn.addTextChangedListener {
            if (ordercheckBtn.text.toString() == "준비중" || ordercheckBtn.text.toString() == "0")
                ordercheckBtn.setTextColor(Color.RED)
            if (ordercheckBtn.text.toString() == "준비완료" || ordercheckBtn.text.toString() == "1")
                ordercheckBtn.setTextColor(Color.parseColor("#786864"))

        }
        //viewHolder.storeImage = viewHolder.itemView.findViewById<ImageView>(R.id.store_image)

        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.binding.orderlist = items[position]

        //Glide.with(holder.itemView.context).load(items[position].storeUrl).into(holder.storeImage!!)

    }

}