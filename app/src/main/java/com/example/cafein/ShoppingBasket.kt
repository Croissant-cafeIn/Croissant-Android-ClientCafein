package com.example.cafein

import Pay
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.menu_item.view.*
import kotlinx.android.synthetic.main.menu_order_list_recyclerview.view.*
import kotlinx.android.synthetic.main.shopping_basket.*
import kotlinx.android.synthetic.main.test_json.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ShoppingBasket : Fragment() {
    companion object {
        lateinit var shoppingBasketView: View
        var orderTotalList = MainHomePage().getOrderTotalList()
        var orderList = arrayListOf<String>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shoppingBasketView = inflater.inflate(R.layout.shopping_basket, container, false)

        // clientId, storeId, orderList, orderDate, status, email, point(total), storeName
        var orderTotalList = MainHomePage().getOrderTotalList()
        ShoppingBasketFragment().setorderList(orderTotalList!![2])


        var fragmentMager = childFragmentManager
        var fragmentTransaction = fragmentMager.beginTransaction()
        fragmentTransaction.replace(R.id.menuOrder, ShoppingBasketFragment()).commit()

        shoppingBasketView.findViewById<TextView>(R.id.price).text = orderTotalList!![6]
        shoppingBasketView.findViewById<TextView>(R.id.storeName).text = orderTotalList!![7]

        shoppingBasketView.findViewById<Button>(R.id.keepShopping).setOnClickListener {
            var orderTotalList = MainHomePage().getOrderTotalList()
            if (orderTotalList[1] != "")
                (activity as MainHomePage).changeFragmentToMenuList()
            else {

                (activity as MainHomePage).changeFragmentKeepShopping()
            }

        }
        shoppingBasketView.findViewById<Button>(R.id.goToShopping).setOnClickListener {
            var intent = Intent(context, Last::class.java)
            intent.putStringArrayListExtra("orderTotalList", orderTotalList)
            startActivity(intent)

        }
        shoppingBasketView.findViewById<Button>(R.id.deleteAll).setOnClickListener {
            var initTotalList = arrayListOf<String>()
            for (i in 0..7)
                initTotalList.add("")
            MainHomePage().setOrderTotalList(initTotalList)
            (activity as MainHomePage).changeFragmentSendOrderList()
        }
        return shoppingBasketView
    }



}