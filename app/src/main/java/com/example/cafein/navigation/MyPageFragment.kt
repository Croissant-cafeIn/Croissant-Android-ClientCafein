package com.example.cafein.navigation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cafein.LoginPage
import com.example.cafein.MainHomePage
import com.example.cafein.R
import com.example.cafein.mypages.*
import kotlinx.android.synthetic.main.login_page.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MyPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lateinit var responseJSONObject: JSONObject
        lateinit var clientId: String
        lateinit var clientEmail: String
        lateinit var clientPassword: String
        var clientPoint: Int = 0

        //var view = LayoutInflater.from(activity).inflate(R.layout.my_page,container,false)
        var view = LayoutInflater.from(activity).inflate(R.layout.my_page,container,false)

        var id = arguments?.getString("id")
        var email = arguments?.getString("email")
        var password = arguments?.getString("password")
        var point = arguments?.getInt("point")
        //var minuspoint = arguments?.getInt("minuspoint")

        /*var favoriteBtn = view.findViewById<Button>(R.id.move_favorite)
        favoriteBtn.setOnClickListener (object :View.OnClickListener {
            override fun onClick(v: View?) {
                    var intent1 = Intent(activity, FavoriteActivity::class.java)
                    startActivity(intent1)
            }
        })*/

        var orderListBtn = view.findViewById<Button>(R.id.move_orderlist)
        orderListBtn.setOnClickListener (object :View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainHomePage?)?.replaceOrderListFragment()
            }
        })

        var noticeBtn = view.findViewById<Button>(R.id.move_notice)
        noticeBtn.setOnClickListener (object :View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainHomePage?)?.replaceNoticeFragment()
            }
        })
        var pointBtn2 = view.findViewById<Button>(R.id.move_point2)
        pointBtn2.text = "보유 포인트 : " + point.toString()

        var pointBtn = view.findViewById<Button>(R.id.move_point)

        pointBtn.setOnClickListener (object :View.OnClickListener {
            override fun onClick(v: View?) {
                val mainhomeactivity = activity as MainHomePage?
                if (mainhomeactivity != null) {
                    mainhomeactivity.replacePointFragment()
                }
            }
        })

        var settingBtn = view.findViewById<Button>(R.id.move_setting)
        settingBtn.setOnClickListener (object :View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainHomePage?)?.replaceSettingFragment()
            }
        })

        val usertext = view.findViewById<TextView>(R.id.user_text)

        var emailsplit = email?.split("@")
        if (email != null) {
            if(email.contains("@"))
                usertext.text = (emailsplit?.get(0) ?: String()) + "님 반갑습니다."
        }
        else
        {
            usertext.text = email + "님 반갑습니다."
        }

        var client = OkHttpClient()
        var jsonInput = JSONObject()


        try {
            jsonInput.put("email", email)
            jsonInput.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        var rb = RequestBody.create(
            MediaType.parse("application/json; charse=UTF-8"),
            jsonInput.toString()
        )

        var request = Request.Builder().url(LoginPage().getServer()+ "client/android/login").post(rb).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    responseJSONObject = JSONObject(response.body()?.string().toString())

                        id = responseJSONObject.getString("id")
                        email = responseJSONObject.getString("email")
                        password = responseJSONObject.getString("password")
                        point = responseJSONObject.getInt("point")
                        Log.d("Login Success", id)
                        pointBtn2.text = "보유 포인트 : " + point.toString()
                    }
                catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
        Thread.sleep(300)

        return view

    }
}
