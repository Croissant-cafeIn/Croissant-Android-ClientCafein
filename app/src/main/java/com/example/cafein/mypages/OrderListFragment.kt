package com.example.cafein.mypages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafein.LoginPage
import com.example.cafein.R
import com.example.cafein.store.OrderList
import com.example.cafein.store.OrderListAdapter
import com.example.cafein.store.Store
import kotlinx.android.synthetic.main.orderlist_page.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class OrderListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.orderlist_page, container, false)
        var getClientId = arguments?.getString("id")
        //Toast.makeText(this.context, LoginPage().getServer() + "order/android/myOrderList/${getClientId}", Toast.LENGTH_LONG).show()
        val orderlists = arrayListOf<OrderList>()

        /*orderlists.add(OrderList(1,1,1,"카페베네","아메리카노(R) 1잔 + 미니케이크 1개",
            "주문일시: 2020.06.08", 1, "총 8000원"))
        orderlists.add(OrderList(1,1,1,"엔제리너스","에스프레소(R) 1잔 + 아메리카노(R)1잔 + 망고주스 1잔",
            "주문일시: 2020.06.08", 1, "총 50000원"))
        orderlists.add(OrderList(1,1,1,"CafeId","초코라떼(R) 2잔 + 녹차라떼(L) 1잔",
            "주문일시: 2020.06.08", 1,"총 100000원"))
        orderlists.add(OrderList(1,1,1,"Cafe2","초코라떼(R) 2잔 + 녹차라떼(L) 1잔",
            "주문일시: 2020.06.08", 1,"총 5000원"))*/


        var orderlistRecyclerView = view.findViewById<RecyclerView>(R.id.orderlist_recyclerView)
        orderlistRecyclerView.layoutManager = LinearLayoutManager(this.context)
        orderlistRecyclerView.adapter = OrderListAdapter(orderlists) { OrderList ->
            Toast.makeText(this.context, LoginPage().getServer() +"order/android/myOrderList/${getClientId}", Toast.LENGTH_LONG).show()
        }
        val mLayoutManager = LinearLayoutManager(this.context)
        mLayoutManager.reverseLayout = true
        mLayoutManager.stackFromEnd = true
        orderlistRecyclerView.layoutManager = mLayoutManager


        var client = OkHttpClient()

        var request = Request.Builder()
            .url(LoginPage().getServer() +"order/android/myOrderList/${getClientId}")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message);
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("TEST", "responseData")

                try {
                    var jsonArray = JSONArray(response.body()?.string().toString())
                    var length = jsonArray.length()
                    for (i in 0..length - 1) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        //var orderId = jsonObject.getInt("order_id")
                        //var clientId = jsonObject.getInt("client_id")
                        var storeId = jsonObject.getInt("storeId")
                        var storeName = jsonObject.getString("storeName")
                        var orderList = jsonObject.getString("orderList")
                        var orderDate = "주문일시: " + jsonObject.getString("orderDate")
                        var orderStatus = jsonObject.getInt("orderStatus")
                        var point = "총 "+jsonObject.getInt("point").toString() + "원"
                        //Toast.makeText(context, storeName, Toast.LENGTH_LONG).show()
                        var getOrderStatus = "준비중"
                        if(orderStatus == 1)
                            getOrderStatus = "준비완료"

                        orderlists.add(
                            OrderList(storeId,storeName,orderList,orderDate,orderStatus,getOrderStatus,point)
                        )

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                //(orderlistRecyclerView.adapter as OrderListAdapter).notifyDataSetChanged()
                Thread.sleep(300)
            }
        })
        Thread.sleep(300)

        return view
    }
}