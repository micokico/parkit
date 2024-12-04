package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.onboarding.PaymentActivity

class NavigationActivity : AppCompatActivity() {

    private lateinit var skipButton: TextView
    private lateinit var nextButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        skipButton = findViewById(R.id.skip_button)
        nextButton = findViewById(R.id.next_button)

        skipButton.setOnClickListener {
            goToLastScreen()
        }

        nextButton.setOnClickListener {
            goToNextScreen()
        }
    }

    private fun goToLastScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToNextScreen() {
        startActivity(Intent(this, PaymentActivity::class.java))
    }
}
