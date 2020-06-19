package com.example.cafein

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.menu_order_list_recyclerview.view.*
import kotlinx.android.synthetic.main.shopping_basket.*

class ShoppingBasketFragment : Fragment() {

    companion object {
        var orderList = ""
    }

    fun getOrderList() = orderList
    fun setorderList(ol: String) {
        orderList = ol
    }

    var menuOrderList = arrayListOf<MenuOrder>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.menu_order_list, container, false)

        var menuOrderRecyclerView = view.findViewById<RecyclerView>(R.id.menuOrderRecyclerview)
        menuOrderRecyclerView.layoutManager = LinearLayoutManager(context)
        menuOrderRecyclerView.adapter = MenuOrderAdapter()

        var orderTotalList = MainHomePage().getOrderTotalList()
        var storeId = orderTotalList[1]
        if (storeId != "") {
            var orderMenu = orderList.split("+")
            for (i in 0..orderMenu.size - 1) {
                menuOrderList.add(MenuOrder(orderMenu[i].split("*")[0], orderMenu[i].split("*")[1]))
            }
            menuOrderRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
        }
        return view
    }

    inner class MenuOrderAdapter :
        RecyclerView.Adapter<ShoppingBasketFragment.MenuOrderAdapter.ViewHolder>() {
        lateinit var view: View
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            view = LayoutInflater.from(context)
                .inflate(R.layout.menu_order_list_recyclerview, parent, false)


            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return menuOrderList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.orderList.text = menuOrderList[position].orderList
            holder.amount.text = menuOrderList[position].amount
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var orderList = v.orderList
            var amount = v.amount

        }

    }
}