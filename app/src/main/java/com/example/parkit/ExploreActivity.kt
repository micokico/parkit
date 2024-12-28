package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ExploreActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var backButton: ImageButton
    private lateinit var parkingItem: LinearLayout

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        // Inicializar os componentes
        searchEditText = findViewById(R.id.search_edit_text)
        nameTextView = findViewById(R.id.nameTextView)
        addressTextView = findViewById(R.id.addressTextView)
        backButton = findViewById(R.id.btn_back)
        parkingItem = findViewById(R.id.parkingItem) // Referenciar o LinearLayout do estacionamento

        // Configurar botão de voltar
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Configurar clique no item do estacionamento
        parkingItem.setOnClickListener {
            val intent = Intent(this, ChooseSpaceActivity::class.java)
            intent.putExtra("PARKING_NAME", nameTextView.text.toString())
            intent.putExtra("PARKING_ADDRESS", addressTextView.text.toString())
            startActivity(intent)
        }

        // Carregar os dados do Firestore
        loadParkingData()
    }

    private fun loadParkingData() {
        db.collection("parkingPrices").document("parking1")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name") ?: "Desconhecido"
                    val address = document.getString("address") ?: "Sem endereço"

                    nameTextView.text = name
                    addressTextView.text = address
                }
            }
            .addOnFailureListener { exception ->
                nameTextView.text = "Erro ao carregar nome"
                addressTextView.text = "Erro ao carregar endereço"
            }
    }
}
