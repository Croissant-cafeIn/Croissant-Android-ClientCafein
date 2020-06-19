package com.example.cafein.navigation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafein.LoginPage
import com.example.cafein.MainHomePage
import com.example.cafein.MenuList
import com.example.cafein.R
import com.example.cafein.store.Store
import com.example.cafein.store.StoreAdapter
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        var view = LayoutInflater.from(activity).inflate(R.layout.home_page, container, false)
        val stores = arrayListOf<Store>()

        var storeRecyclerView = view.findViewById<RecyclerView>(R.id.store_recyclerView)
        storeRecyclerView.layoutManager = LinearLayoutManager(context)
        storeRecyclerView.adapter = StoreAdapter(stores) { Store ->
            (activity as MainHomePage).changeFragment(store = Store)
        }

        var client = OkHttpClient()

        var request = Request.Builder()
            .url(LoginPage().getServer() +"store/android/list")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message);
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("TEST", "responseDatae")

                try {
                    var jsonArray = JSONArray(response.body()?.string().toString())
                    var length = jsonArray.length()
                    for (i in 0..length - 1) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        if (jsonObject.getString("location").contains("성북구")) {
                            var id = jsonObject.getInt("id")
                            var name = jsonObject.getString("name")
                            var location = jsonObject.getString("location")
                            var theme = jsonObject.getString("theme")
                            var ownerId = jsonObject.getString("ownerId")
                            var storeUrl = jsonObject.getString("storeUrl")

                            var congestion = jsonObject.getInt("congestion").toString()
                            if(congestion== "0")
                                congestion = "여유"
                            if(congestion == "1")
                                congestion = "보통"
                            if(congestion == "2")
                                congestion ="혼잡"

                            stores.add(Store(id,name,location,theme,ownerId,storeUrl,congestion))

                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        })




        val searchTxt: EditText = view.findViewById(R.id.searchText)
        val searchBtn: Button = view.findViewById(R.id.searchButton)
        searchBtn.setOnClickListener {
            if (stores.isNotEmpty())
                stores.clear()
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
                            if (jsonObject.getString("location").contains("성북구")) {
                                if (jsonObject.getString("name").contains(searchTxt.text.toString())
                                    || jsonObject.getString("location")
                                        .contains(searchTxt.text.toString())
                                ) {

                                    var id = jsonObject.getInt("id")
                                    var name = jsonObject.getString("name")

                                    var location = jsonObject.getString("location")
                                    var theme = jsonObject.getString("theme")
                                    var ownerId = jsonObject.getString("ownerId")
                                    var storeUrl = jsonObject.getString("storeUrl")
                                    var congestion = jsonObject.getInt("congestion").toString()
                                    if(congestion == "0")
                                        congestion = "여유"
                                    if(congestion == "1")
                                        congestion = "보통"
                                    if(congestion == "2")
                                        congestion = "혼잡"
                                    stores.add(
                                        Store(
                                            id,
                                            name,
                                            location,
                                            theme,
                                            ownerId,
                                            storeUrl,
                                            congestion
                                        )
                                    )
                                }
                            }

                            //val congestionTxt = view.findViewById<TextView>(R.id.congestionButton)
                            //congestionTxt.setTextColor(Color.RED)
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            })
            
            (storeRecyclerView.adapter as StoreAdapter).notifyDataSetChanged()
            Thread.sleep(300)

        }

        val curlocation: TextView = view.findViewById(R.id.currentLocation)
        curlocation.text = "현재 위치: 서울특별시 성북구 삼선교로 1 (한성대입구역)"
        Thread.sleep(300)
        //view.findViewById<TextView>(R.id.congestionButton)?.setTextColor(Color.RED)
        //view.findViewById<EditText>(R.id.editText2).requestFocus()
        return view

    }


}
