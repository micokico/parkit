package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class GlobalRevenueActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_revenue)

        val revenueTotalTextView: TextView = findViewById(R.id.revenue_total)

        db = FirebaseFirestore.getInstance()

        calculateTotalRevenue { totalRevenue ->
            revenueTotalTextView.text = String.format("%.2f €", totalRevenue)
        }

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun calculateTotalRevenue(callback: (Double) -> Unit) {
        db.collection("reservations")
            .get()
            .addOnSuccessListener { result ->
                var total = 0.0
                for (document in result) {
                    val preco = document.getDouble("totalCost") ?: 0.0
                    total += preco
                }
                callback(total)
            }
            .addOnFailureListener { exception ->
                Log.e("GlobalRevenueActivity", "Erro ao buscar dados: ", exception)
                callback(0.0)
            }
    }
}
