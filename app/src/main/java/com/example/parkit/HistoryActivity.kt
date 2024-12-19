package com.example.parkit

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var parkingImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var priceTextView: TextView
    private val recentList = mutableListOf<Parking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        parkingImageView = findViewById(R.id.parkingImageView)
        nameTextView = findViewById(R.id.nameTextView)
        addressTextView = findViewById(R.id.addressTextView)
        priceTextView = findViewById(R.id.priceTextView)

        loadRecentHistory()

        searchEditText.addTextChangedListener {
            val query = it.toString()
            filterResults(query)
        }
    }

    private fun filterResults(query: String) {
        val filteredList = recentList.filter {
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
        }
        if (filteredList.isNotEmpty()) {
            updateUI(filteredList[0]) // Atualiza com o primeiro item filtrado
        }
    }

    private fun loadRecentHistory() {
        val db = FirebaseFirestore.getInstance()
        val userPhone = "918235917" // Número de telefone do usuário

        db.collection("users").document(userPhone).collection("history").get()
            .addOnSuccessListener { result ->
                recentList.clear()
                for (document in result) {
                    val name = document.getString("name") ?: "Unknown"
                    val address = document.getString("address") ?: "Unknown"
                    val price = document.getDouble("price") ?: 0.0
                    val parking = Parking(name, address, price)
                    recentList.add(parking)
                }
                if (recentList.isNotEmpty()) {
                    updateUI(recentList[0]) // Atualiza com o primeiro item da lista
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao carregar histórico", exception)
                Toast.makeText(this, "Erro ao carregar histórico", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(parking: Parking) {
        nameTextView.text = parking.name
        addressTextView.text = parking.address
        priceTextView.text = String.format("$%.2f", parking.price)
        // Adicione uma imagem se necessário (exemplo: parkingImageView.setImageResource(...))
    }
}

fun saveToHistory(name: String, address: String, price: Double) {
    val db = FirebaseFirestore.getInstance()
    val userPhone = "918235917" // Número de telefone do usuário

    val parkingData = hashMapOf(
        "name" to name,
        "address" to address,
        "price" to price
    )

    db.collection("users").document(userPhone).collection("history")
        .add(parkingData)
        .addOnSuccessListener {
            Log.d("Firestore", "Histórico salvo com sucesso")
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Erro ao salvar histórico", exception)
        }
}

// Classe Parking ajustada para refletir o modelo usado no Firestore
data class Parking(
    val name: String = "",
    val address: String = "",
    val price: Double = 0.0
)