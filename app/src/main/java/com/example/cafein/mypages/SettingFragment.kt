package com.example.cafein.mypages

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cafein.LoginPage
import com.example.cafein.MainHomePage
import com.example.cafein.Map
import com.example.cafein.R
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.setting_page, container, false)

        var clientId = arguments?.getString("id")
        var clientEmail = arguments?.getString("email")
        var clientPassword = arguments?.getString("password")
        var point = arguments?.getInt("point")

        var changePasswordBtn = view.findViewById<Button>(R.id.changePasswordBtn)
        changePasswordBtn.setOnClickListener {
            var client = OkHttpClient()

            var jsonInput = JSONObject()
            var currentPassword = view.findViewById<EditText>(R.id.currenPassword)
            var changePassword = view.findViewById<EditText>(R.id.changePassword1)
            var changePasswordCheck = view.findViewById<EditText>(R.id.chagePasswordCheck2)
            try {
                //Toast.makeText(context, "" + clientPassword + currentPassword.text.toString() + changePassword.text.toString(), Toast.LENGTH_LONG).show()
                if (clientPassword != null) {
                    if (clientPassword == currentPassword.text.toString() && changePassword.text.toString() == changePasswordCheck.text.toString()) {
                        jsonInput.put("email", clientEmail)
                        jsonInput.put("password", changePassword.text.toString())

                        val intent = Intent(this.context, MainHomePage::class.java)
                        intent.putExtra("id", clientId)
                        intent.putExtra("email", clientEmail)
                        intent.putExtra("password", changePassword.text.toString())
                        intent.putExtra("point", point)
                        Toast.makeText(this.context, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_LONG).show()
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this.context, "비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show()
                    }
                }


            } catch (e: JSONException) {
                e.printStackTrace()
            }


            var rb = RequestBody.create(
                MediaType.parse("application/json; charse=UTF-8"),
                jsonInput.toString()
            )
            var request =
                Request.Builder().url(LoginPage().getServer() +"client/android/updatePassword").post(rb).build()
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