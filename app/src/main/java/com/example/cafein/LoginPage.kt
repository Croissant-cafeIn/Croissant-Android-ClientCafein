package com.example.cafein

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.login_page.id
import kotlinx.android.synthetic.main.login_page.password
import kotlinx.android.synthetic.main.sign_up_page.*
import kotlinx.android.synthetic.main.test_json.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginPage : AppCompatActivity() {
    lateinit var responseJSONObject: JSONObject
    lateinit var clientId: String
    lateinit var clientEmail: String
    lateinit var clientPassword: String
    var clientPoint: Int = 0
    companion object{
        //var server = "http://172.30.1.37:9090/"
        //var server = "http://172.20.10.8:9090/"
        //val server = "http://172.20.10.8:9090/"
        val server = "http://10.0.2.2:9090/"
    }
    fun getServer() = server

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        login.setOnClickListener {
            doLogin()

        }

        signUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
        }

        password.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
        password.transformationMethod = PasswordTransformationMethod.getInstance()

    }

    fun doLogin() {
        var client = OkHttpClient()
        var jsonInput = JSONObject()


        try {
            jsonInput.put("email", id.text.toString())
            jsonInput.put("password", password.text.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        var rb = RequestBody.create(
            MediaType.parse("application/json; charse=UTF-8"),
            jsonInput.toString()
        )

        var request = Request.Builder().url(getServer() + "client/android/login").post(rb).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    responseJSONObject = JSONObject(response.body()?.string().toString())

                    if (responseJSONObject.has("message") && (responseJSONObject.getString("message") == "client 이름을 찾을 수 없습니다.")) {
                        var handler = Handler(Looper.getMainLooper())
                        handler.post(Runnable {
                            kotlin.run {
                                Toast.makeText(
                                    this@LoginPage,
                                    "존재하지 않는 아이디입니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        })
                    } else if (responseJSONObject.has("message") && (responseJSONObject.getString("message") == "비밀번호가 맞지 않습니다.")) {
                        var handler = Handler(Looper.getMainLooper())
                        handler.post(Runnable {
                            kotlin.run {
                                Toast.makeText(
                                    this@LoginPage,
                                    "잘못된 비밀번호입니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        })
                    } else {
                        clientId = responseJSONObject.getString("id")
                        clientEmail = responseJSONObject.getString("email")
                        clientPassword = responseJSONObject.getString("password")
                        clientPoint = responseJSONObject.getInt("point")
                        Log.d("Login Success", clientId)

                        val intent = Intent(applicationContext, MainHomePage::class.java)
                        intent.putExtra("id", clientId)
                        intent.putExtra("email", clientEmail)
                        intent.putExtra("password", clientPassword)
                        intent.putExtra("point", clientPoint)
                        startActivity(intent)
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        })
    }


}