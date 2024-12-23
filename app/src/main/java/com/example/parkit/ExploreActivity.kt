package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ExploreActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var backButton: ImageButton

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        // Inicializando as views
        searchEditText = findViewById(R.id.search_edit_text)
        nameTextView = findViewById(R.id.nameTextView)
        addressTextView = findViewById(R.id.addressTextView)
        backButton = findViewById(R.id.btn_back)

        // Configurando botão de voltar para ir à HomeActivity
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Carregar dados do Firestore (apenas name e address)
        loadParkingData()
    }

    private fun loadParkingData() {
        db.collection("parkingPrices").document("parking1")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Obter apenas o name e o address
                    val name = document.getString("name") ?: "Desconhecido"
                    val address = document.getString("address") ?: "Sem endereço"

                    // Atualizar a interface
                    nameTextView.text = name
                    addressTextView.text = address
                }
            }
            .addOnFailureListener { exception ->
                // Tratar falha no carregamento de dados
                nameTextView.text = "Erro ao carregar nome"
                addressTextView.text = "Erro ao carregar endereço"
            }
    }
}
