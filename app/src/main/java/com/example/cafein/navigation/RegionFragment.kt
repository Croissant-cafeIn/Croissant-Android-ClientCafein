package com.example.cafein.navigation

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
import com.example.cafein.R
import com.example.cafein.store.Store
import com.example.cafein.store.StoreBigAdapter
import kotlinx.android.synthetic.main.region_page.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class RegionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.region_page, container, false)

        //var getCity = intent.getStringExtra("city")
        var getNumber = arguments?.getInt("number")



        val list1 = ArrayList<String>()
        list1.add("서울")
        //list1.add("경기")
        val list2 = ArrayList<String>()
        list2.add("강남구/서초구")
        list2.add("강동구/송파구")
        list2.add("동작구/관악구/영등포구")
        list2.add("강서구/양천구")
        list2.add("구로구/금천구")
        list2.add("마포구/용산구")
        list2.add("은평구/서대문구")
        list2.add("종로구/중구")
        list2.add("성동구/동대문구/성북구")
        list2.add("중랑구/광진구")
        list2.add("강북구/도봉구/노원구")

        val list3 = ArrayList<String>()
        list3.add("ALL")
        list3.add("24시 카페")
        list3.add("스터디 카페")
        list3.add("디저트 카페")
        /*list3.add("24/study/dessert") // 나중에 dessert로 수정
        list3.add("24")
        list3.add("study")
        list3.add("dessert")*/

        val adapter1 = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item, list1)
        val adapter2 = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item, list2)
        val adapter3 = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item, list3)

        val cspinner = view.findViewById<Spinner>(R.id.citySpinner)
        val gspinner= view.findViewById<Spinner>(R.id.guSpinner)
        val tspinner = view.findViewById<Spinner>(R.id.themeSpinner)

        cspinner.adapter = adapter1
        gspinner.adapter = adapter2
        tspinner.adapter = adapter3

        if (getNumber != null) {
            gspinner.setSelection(getNumber)
        }
        //var splitarr = getCity.split("/")

        var splitarr = gspinner.selectedItem.toString().split("/")
        var selcTheme = tspinner.selectedItem.toString()
        var splitTheme = "24,study,dessert".split(",")
        /*for (j in 0..splitarr.size - 1)
            Toast.makeText(this, "" + splitarr[j], Toast.LENGTH_LONG).show()*/

        val stores = arrayListOf<Store>()

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
                Log.d("TEST", "responseData")

                try {
                    var jsonArray = JSONArray(response.body()?.string().toString())
                    var length = jsonArray.length()
                    for (i in 0..length - 1) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        for (j in 0..splitarr.size - 1) {
                            if (jsonObject.getString("location").contains(splitarr[j])) {

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
                                var themeKor = "테마: 24시, 스터디, 디저트 카페"
                                if(theme.equals("24"))
                                    themeKor = "테마: 24시 카페"
                                if(theme.equals("study"))
                                    themeKor = "테마: 스터디 카페"
                                if(theme.equals("dessert"))
                                    themeKor = "테마: 디저트 카페"
                                if(theme.equals("24,study"))
                                    themeKor="테마: 24시, 스터디 카페"
                                if(theme.equals("24,dessert"))
                                    themeKor="테마: 24시, 디저트 카페"
                                if(theme.equals("study,dessert"))
                                    themeKor="테마: 스터디, 디저트 카페"
                                if(theme.equals("24,study,dessert"))
                                    themeKor="테마: 24시, 스터디, 디저트 카페"

                                stores.add(
                                    Store(
                                        id,
                                        name,
                                        location,
                                        themeKor,
                                        ownerId,
                                        storeUrl,
                                        congestion
                                    )
                                )
                            }
                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        })


        var storeRecyclerView = view.findViewById<RecyclerView>(R.id.store_recyclerView)
        storeRecyclerView.layoutManager = LinearLayoutManager(this.context)
        storeRecyclerView.adapter = StoreBigAdapter(stores) { Store ->
            //MenuList를 Fragment로 변경하는 코드 작성해주세요
            (activity as MainHomePage).changeFragment(store = Store)
        }
        Thread.sleep(300)

        gspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                splitarr = guSpinner.selectedItem.toString().split("/")
            }
        }
        tspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selcTheme = themeSpinner.selectedItem.toString()
                if(selcTheme.equals("ALL"))
                    splitTheme = "24,study,dessert/24,study/24,dessert/study,dessert/24/study/dessert".split("/")
                if(selcTheme.equals("24시 카페"))
                    splitTheme = "24,study,dessert/24,study/24,dessert/24".split("/")
                if(selcTheme.equals("스터디 카페"))
                    splitTheme = "24,study,dessert/24,study/study,dessert/study".split("/")
                if(selcTheme.equals("디저트 카페"))
                    splitTheme = "24,study,dessert/24,dessert/dessert/study,dessert".split("/")
                /*list3.add("24/study/dessert") // 나중에 dessert로 수정
                list3.add("24")
                list3.add("study")
                list3.add("dessert")*/


                //Toast.makeText(this@RegionActivity, "" + splitTheme, Toast.LENGTH_LONG).show()
            }
        }
        val searchByName = view.findViewById<EditText>(R.id.searchName)
        val searchBtn = view.findViewById<Button>(R.id.searchButton2)
        searchBtn.setOnClickListener {
            if(stores.isNotEmpty())
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
                            for (j in 0..splitarr.size - 1) {
                                for (k in 0..splitTheme.size - 1) {
                                    if (jsonObject.getString("location").contains(splitarr[j])
                                        && jsonObject.getString("theme") == splitTheme[k]
                                        && jsonObject.getString("name").contains(searchByName.text.toString())) {

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
                                        var themeKor = "테마: 24시, 스터디, 디저트 카페"
                                        if(theme.equals("24"))
                                            themeKor = "테마: 24시 카페"
                                        if(theme.equals("study"))
                                            themeKor = "테마: 스터디 카페"
                                        if(theme.equals("dessert"))
                                            themeKor = "테마: 디저트 카페"
                                        if(theme.equals("24,study"))
                                            themeKor="테마: 24시, 스터디 카페"
                                        if(theme.equals("24,dessert"))
                                            themeKor="테마: 24시, 디저트 카페"
                                        if(theme.equals("study,dessert"))
                                            themeKor="테마: 스터디, 디저트 카페"
                                        if(theme.equals("24,study,dessert"))
                                            themeKor="테마: 24시, 스터디, 디저트 카페"

                                        stores.add(
                                            Store(
                                                id,
                                                name,
                                                location,
                                                themeKor,
                                                ownerId,
                                                storeUrl,
                                                congestion
                                            )
                                        )
                                    }
                                }


                            }
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            })
            (storeRecyclerView.adapter as StoreBigAdapter).notifyDataSetChanged()
            Thread.sleep(300)
        }
        return view
    }
}