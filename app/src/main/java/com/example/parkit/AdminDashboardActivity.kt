package com.example.parkit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.example.parkit.databinding.ActivityAdminBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        loadParkStatistics()
    }

    private fun loadParkStatistics() {
        db.collection("parkStatistics").document("dailyStats")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val totalVisitors = document.getLong("totalVisitors") ?: 0
                    binding.totalVisitorsTextView.text = "Total de Visitantes Hoje: $totalVisitors"
                } else {
                    binding.totalVisitorsTextView.text = "Estatísticas indisponíveis."
                }
            }
            .addOnFailureListener { e ->
                binding.totalVisitorsTextView.text = "Erro ao carregar estatísticas: ${e.message}"
            }
    }
}
