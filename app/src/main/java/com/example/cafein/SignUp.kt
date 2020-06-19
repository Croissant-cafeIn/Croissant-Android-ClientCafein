package com.example.cafein

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.InputStreamBitmapImageDecoderResourceDecoder
import com.google.android.gms.common.util.JsonUtils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.sign_up_page.*
import kotlinx.android.synthetic.main.sign_up_page.id
import kotlinx.android.synthetic.main.sign_up_page.password
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.StringBuilder
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.WatchEvent

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(password.text.toString().length < 4){
                    passwordRule.visibility = View.VISIBLE
                } else {
                    passwordRule.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        password.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
        password.transformationMethod = PasswordTransformationMethod.getInstance()


        passwordAgain.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(passwordAgain.text.toString() != password.text.toString()){
                    Log.e("password", passwordAgain.text.toString())
                    passwordAgainRule.visibility = View.VISIBLE
                } else {
                    passwordAgainRule.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        passwordAgain.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
        passwordAgain.transformationMethod = PasswordTransformationMethod.getInstance()

        signUpButton.setOnClickListener {
            updateUserInfo()
            var intent = Intent(applicationContext, LoginPage::class.java)
            startActivity(intent)
        }

    }
    fun updateUserInfo() {
        var client = OkHttpClient()

        var jsonInput = JSONObject()

        try{
            jsonInput.put("email",  id.text.toString())
            jsonInput.put("password",  password.text.toString())
        } catch (e: JSONException){
            e.printStackTrace()
        }


        var rb = RequestBody.create(
            MediaType.parse("application/json; charse=UTF-8"),
            jsonInput.toString()
        )

        var request = Request.Builder().url(LoginPage().getServer() + "client/android/new").post(rb).build()

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


}