package com.project.trux_application.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.project.trux_application.R

class MainActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler().postDelayed({
            val mainIntent = Intent(this, LoginPageActivity::class.java) //Todo Login
            startActivity(mainIntent)
            finish()
        }, SPLASH_DELAY)
    }


}