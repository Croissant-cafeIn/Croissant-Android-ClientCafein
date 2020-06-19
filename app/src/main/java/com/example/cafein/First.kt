package com.example.cafein

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.cafein.R.layout.first

class First : AppCompatActivity(){
    val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(first)

        Handler().postDelayed({
            startActivity((Intent(this, LoginPage::class.java)))
            finish()
        }, SPLASH_TIME_OUT)
    }
}