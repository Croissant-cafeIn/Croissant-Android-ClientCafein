package com.example.cafein.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafein.MainHomePage
import com.example.cafein.R


class RegionSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.region_page_select,container,false)

        val guList = arrayListOf<City>()
        guList.add(City("강남구/서초구", 0))
        guList.add(City("강동구/송파구", 1))
        guList.add(City("동작구/관악구/영등포구", 2))
        guList.add(City("강서구/양천구", 3))
        guList.add(City("구로구/금천구", 4))
        guList.add(City("마포구/용산구", 5))
        guList.add(City("은평구/서대문구", 6))
        guList.add(City("종로구/중구", 7))
        guList.add(City("성동구/동대문구/성북구", 8))
        guList.add(City("중랑구/광진구", 9))
        //guList.add(City("강북구/도봉구/노원구", 10))
        guList.add(City("강북구/도봉구/노원구", 10)) //뭐지? 2개해야 보이네



        val guRecyclerView = view.findViewById<RecyclerView>(R.id.gu_recycler)
        guRecyclerView.layoutManager = LinearLayoutManager(this.context)
        guRecyclerView.adapter = RegionSelectAdapter(guList) {City ->
            Toast.makeText(this.context, "${City.name}", Toast.LENGTH_LONG).show()

            val mainhomeactivity = activity as MainHomePage?
            if (mainhomeactivity != null) {
                mainhomeactivity.replaceRegionFragment(city = City)
            }

        }


        return view
    }




}