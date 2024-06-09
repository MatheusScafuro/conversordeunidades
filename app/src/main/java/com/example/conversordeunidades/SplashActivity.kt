package com.example.conversordeunidades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val enterButton = findViewById<Button>(R.id.enterButton)
        enterButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
