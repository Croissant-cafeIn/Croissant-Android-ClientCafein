package com.example.cafein.mypages

import android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cafein.*
import com.example.cafein.navigation.MyPageFragment
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class PointFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.point_page, container, false)
        var clientId = arguments?.getString("id")
        var clientEmail = arguments?.getString("email")
        var clientPassword = arguments?.getString("password")
        var point = arguments?.getInt("point")


        /*var clientId = intent.getStringExtra("id").toString()
        var clientEmail = intent.getStringExtra("email").toString()
        var clientPassword = intent.getStringExtra("password").toString()
        var point = intent.getStringExtra("point").toString()*/
        var currentPoint = view.findViewById<TextView>(R.id.currenPoint)
        var addPoint = view.findViewById<EditText>(R.id.addPoint)
        addPoint.setText("0")
        currentPoint.text = point.toString() + " 포인트"
        var addPointButton = view.findViewById<Button>(R.id.addPointButton)
        addPointButton.setOnClickListener {
            var client = OkHttpClient()

            var jsonInput = JSONObject()

            try {
                if (addPoint.text.toString().toInt() > 0) {
                    jsonInput.put("email", clientEmail)
                    //jsonInput.put("password",  clientPassword)
                    jsonInput.put("point", addPoint.text.toString().toInt())
                    point = point?.plus(addPoint.text.toString().toInt())
                    // point = ((point?.toInt() ?: Int).toString().toInt() + addPoint.text.toString().toInt()).toString()
                    currentPoint.text = point.toString() + " 포인트"


                    Toast.makeText(this.context, "포인트 충전이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    //point?.let { it1 -> (activity as MainHomePage?)?.chargePointFragment(it1) }
                    //(activity as MainHomePage?)?.supportFragmentManager?.popBackStackImmediate("point", POP_BACK_STACK_INCLUSIVE)

                    val intent = Intent(this.context, MainHomePage::class.java)
                    intent.putExtra("id", clientId)
                    intent.putExtra("email", clientEmail)
                    intent.putExtra("password", clientPassword)
                    intent.putExtra("point", point)
                    Toast.makeText(this.context, "포인트 충전이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    Toast.makeText(this.context, "포인트 충전은 0원 이상 해야 합니다.", Toast.LENGTH_LONG).show()
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }


            var rb = RequestBody.create(
                MediaType.parse("application/json; charse=UTF-8"),
                jsonInput.toString()
            )

            var request =
                Request.Builder().url(LoginPage().getServer() +"client/android/addPoint").post(rb).build()
            //  Request.Builder().url("http://10.0.2.2:9090/client/android/addPoint").post(rb).build() 최신DB에서 이거로 변경
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("TEST", "ERROR Message : " + e.message);
                }

                override fun onResponse(call: Call, response: Response) {
                    var responseData = response.body().toString()
                    Log.d("TEST", "responseDatae : " + responseData)


                }
            })


        }

        return view
    }
}