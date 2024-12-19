package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ExploreActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var backButton: ImageButton

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        // Inicializando as views
        searchEditText = findViewById(R.id.search_edit_text)
        nameTextView = findViewById(R.id.nameTextView)
        addressTextView = findViewById(R.id.addressTextView)
        priceTextView = findViewById(R.id.priceTextView)
        backButton = findViewById(R.id.btn_back)

        // Configurando botão de voltar para ir à HomeActivity
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Carregar dados do Firebase
        loadParkingFromFirebase()

        // Filtro de pesquisa
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterResults(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadParkingFromFirebase() {
        db.collection("parking").get()
            .addOnSuccessListener { result ->
                if (result.documents.isNotEmpty()) {
                    val parking = result.documents[0].toObject(Parking::class.java)
                    parking?.let {
                        updateUI(it)
                    }
                }
            }
            .addOnFailureListener {
                // Trate o erro de carregamento
            }
    }

    private fun filterResults(query: String) {
        db.collection("parking")
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", query + "\uf8ff")
            .get()
            .addOnSuccessListener { result ->
                if (result.documents.isNotEmpty()) {
                    val parking = result.documents[0].toObject(Parking::class.java)
                    parking?.let {
                        updateUI(it)
                    }
                }
            }
            .addOnFailureListener {
                // Trate o erro de pesquisa
            }
    }

    private fun updateUI(parking: Parking) {
        nameTextView.text = parking.name
        addressTextView.text = parking.address
        priceTextView.text = String.format("%.2f €/hora", parking.price)
    }
}
