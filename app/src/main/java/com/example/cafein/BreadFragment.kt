package com.example.cafein

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.menu_item.view.*
import kotlinx.android.synthetic.main.menu_list.*
import kotlinx.android.synthetic.main.test_json.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class BreadFragment : Fragment() {
    lateinit var v: View

    companion object {
        var imageUrlList = arrayListOf<String>()
        var menuJsonList = arrayListOf<JSONObject>()
        var bundle: Bundle? = null
    }


    fun getMenu() {
        var client = OkHttpClient()

        var request = Request.Builder()
            .url(LoginPage().getServer() + "store/android/list/" + MenuList().getStoreId())
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message);
            }

            override fun onResponse(call: Call, response: Response) {
                var jsonList = arrayListOf<JSONObject>()
                Log.d("TEST", "SUCCESS!")
                try {
                    var jsonArray = JSONArray(response.body()?.string().toString())
                    var length = jsonArray.length()

                    for (i in 0..length - 1) {
                        if(jsonArray.length() == 0) {
                            var handler = Handler(Looper.getMainLooper())
                            handler.post(Runnable {
                                kotlin.run {
                                    MenuList.noDessertView.visibility = View.VISIBLE
                                }
                            })
                        }
                        var jsonObject = jsonArray.getJSONObject(i)
                        if ((jsonObject.getString("item") == "Bread")
                            || jsonObject.getString("item") == "Icecream"
                        ) {
                            jsonList.add(jsonObject)
                            imageUrlList.add(jsonObject.getString("menuUrl"))
                        }

                    }
                    menuJsonList = jsonList
                    Log.e("listsize", menuJsonList.size.toString())

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getMenu()

        v = inflater.inflate(R.layout.menu_recycler, container, false)
        //1
        var menuRecyclerView = v.findViewById<RecyclerView>(R.id.menuRecyclerView)
        //2
        menuRecyclerView.layoutManager = LinearLayoutManager(context)
        //3
        menuRecyclerView.adapter = MenuListAdapter()
        menuRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )

        return v
    }


    inner class MenuListAdapter : RecyclerView.Adapter<MenuListAdapter.ViewHolder>() {
        lateinit var view: View


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return menuJsonList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.menuName.text = menuJsonList[position].getString("name")
            holder.menuName.setOnClickListener(RecyclerViewOnClickListener(menuJsonList[position]))
            holder.menuPrice.text = menuJsonList[position].getString("price").split(",")[0] + "~"
            Log.e("price", menuJsonList[position].getString("price"))

            Glide.with(context!!)
                .load(menuJsonList[position].getString("menuUrl"))
                .override(100, 100)
                .into(holder.menuImage)

        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var menuName = v.menu_name
            var menuPrice = v.menu_pay
            var menuImage = v.menu_image

        }

        inner class RecyclerViewOnClickListener(menu: JSONObject) :
            View.OnClickListener {
            var menu = menu
            override fun onClick(v: View?) {
                //MainHomePage().changeFragment(menu)
                //MenuList().changeFragment(menu)
                val mainhomeactivity = activity as MainHomePage?
                if (mainhomeactivity != null) {
                    MainHomePage().setMenuJson(menu)
                    mainhomeactivity.changeFragmentBeverage(menu)
                }
            }
        }
    } // inner class adapter


}