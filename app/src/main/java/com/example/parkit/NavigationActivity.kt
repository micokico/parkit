package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val skipButton: TextView = findViewById(R.id.skip_button)
        val nextButton: TextView = findViewById(R.id.next_button)

        skipButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        nextButton.setOnClickListener {
            startActivity(Intent(this, PayamentActivity::class.java))
        }
    }
}
