package com.example.cafein

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.last.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Last: AppCompatActivity(){
    var paymentList = mutableListOf<Payment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.last)

        var jsonObject = JSONObject()
        var orderTotalList = intent.getStringArrayListExtra("orderTotalList")

        jsonObject.put("clientId", orderTotalList[0].toInt())
        jsonObject.put("storeId", orderTotalList[1].toInt())
        jsonObject.put("orderList", orderTotalList[2])
        jsonObject.put(
            "orderDate",
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date(System.currentTimeMillis()))
        )
        jsonObject.put("status", orderTotalList[4].toInt())
        jsonObject.put("email", orderTotalList[5])
        jsonObject.put("point", orderTotalList[6].toInt())
        jsonObject.put("storeName", orderTotalList[7])
        Log.e("date", SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date(System.currentTimeMillis())))

        finish.text = jsonObject.getInt("point").toString() + "원 결제하기"

        paymentList.add(Payment("토스"))
        paymentList.add(Payment("신용카드"))
        paymentList.add(Payment("직접 결제"))
        paymentList.add(Payment("네이버 결제"))
        paymentList.add(Payment("카카오 페이"))

        finish.setOnClickListener {
            Thread().run() {
                sendShoppingList(jsonObject)
            }
            var list = arrayListOf<String>()
            for(i in 0..7)
                list.add("")
            MainHomePage().setOrderTotalList(list)


            var intent = Intent()
            intent.setClassName("com.example.ownercafein", "com.example.ownercafein.MainActivity")
            intent.putExtra("go", "주문이 들어왔습니다.")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivityForResult(intent, 0)
        }

    }

    fun sendShoppingList(otl: JSONObject) {
        var client = OkHttpClient()

        var rb = RequestBody.create(
            MediaType.parse("application/json; charse=UTF-8"),
            otl.toString()
        )

        var request = Request.Builder()
            .url(LoginPage().getServer() + "order/android/new")
            .post(rb)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {

                Log.d("Send to Server", "SUCCESS!!!!!!!!!!!!!!!!!!!!!!!!!!!")

            }
        })
    }
}