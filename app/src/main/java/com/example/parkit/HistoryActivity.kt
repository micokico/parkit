package com.example.parkit

import android.os.Bundle
import android.util.Log
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
        val backButton = findViewById<ImageView>(R.id.btn_back) // Botão de voltar

        // Configurar o botão de voltar
        backButton.setOnClickListener {
            finish() // Finaliza a atividade atual e volta à anterior
        }

        loadRecentHistory()

        searchEditText.addTextChangedListener {
            val query = it.toString()
            filterResults(query)
        }
    }

    private fun updateUI(parking: Parking) {
        nameTextView.text = parking.name
        addressTextView.text = parking.address
        priceTextView.text = String.format("€%.2f", parking.price) // Preço formatado

        // Exibe informações adicionais
        findViewById<TextView>(R.id.dateTextView).text = "Data: ${parking.date}"
        findViewById<TextView>(R.id.durationTextView).text = "Duração: ${parking.duration} horas"
        findViewById<TextView>(R.id.transportTextView).text = "Transporte: ${parking.transport.capitalize()}"
    }

    private fun filterResults(query: String) {
        val filteredList = recentList.filter {
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
        }
        if (filteredList.isNotEmpty()) {
            updateUI(filteredList[0])
        }
    }

    private fun loadRecentHistory() {
        val db = FirebaseFirestore.getInstance()

        // Acessa o documento "history" dentro da subcoleção "history" do documento "918235917"
        db.collection("users").document("918235917").collection("history").document("history").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    recentList.clear() // Limpa a lista antes de adicionar novos resultados

                    val name = document.getString("name") ?: "Unknown"
                    val address = document.getString("address") ?: "Unknown"
                    val priceString = document.getString("preco") ?: "0" // Preço como String
                    val price = priceString.toDoubleOrNull() ?: 0.0 // Converte para Double
                    val date = document.getString("data") ?: "Unknown"
                    val durationString = document.getString("duracao") ?: "0"
                    val duration = durationString.toDoubleOrNull() ?: 0.0
                    val transport = document.getString("type") ?: "Unknown"

                    val parking = Parking(name, address, price, date, duration, transport)

                    recentList.add(parking) // Adiciona aos dados recentes

                    // Atualiza a interface com os dados do documento
                    updateUI(parking)
                } else {
                    Toast.makeText(this, "Nenhum histórico encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao carregar histórico", exception)
                Toast.makeText(this, "Erro ao carregar histórico", Toast.LENGTH_SHORT).show()
            }
    }

    fun saveToHistory(name: String, address: String, duration: Double, transport: String) {
        val db = FirebaseFirestore.getInstance()
        val userPhone = "918235917" // Número de telefone do usuário

        // Preços por tipo de transporte
        val transportPrices = mapOf(
            "bike" to 1.0,
            "car" to 2.0,
            "scooter" to 1.5,
            "van" to 3.0
        )

        // Calcula o preço com base no tempo e tipo de transporte
        val pricePerHour = transportPrices[transport] ?: 0.0
        val price = pricePerHour * duration

        // Obtém a data atual
        val currentDate = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date())

        val parkingData = hashMapOf(
            "name" to name,
            "address" to address,
            "preco" to price.toString(), // Preço total como String
            "data" to currentDate,
            "duracao" to duration.toString(),
            "type" to transport
        )

        // Atualiza o documento "history" dentro da subcoleção "history"
        db.collection("users").document(userPhone).collection("history").document("history")
            .set(parkingData)
            .addOnSuccessListener {
                Log.d("Firestore", "Histórico salvo com sucesso")
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao salvar histórico", exception)
            }
    }
}

data class Parking(
    val name: String = "",
    val address: String = "",
    val price: Double = 0.0,
    val date: String = "", // Data de entrada/saída
    val duration: Double = 0.0, // Duração em horas
    val transport: String = "" // Meio de transporte usado
)
