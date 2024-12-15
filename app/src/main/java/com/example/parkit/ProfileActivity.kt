package com.example.parkit

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var valueName: TextView
    private lateinit var valueEmail: TextView
    private lateinit var valuePhone: TextView
    private lateinit var saveButton: Button

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inicializar os componentes
        profileImage = findViewById(R.id.profileImage)
        valueName = findViewById(R.id.valueName)
        valueEmail = findViewById(R.id.valueEmail)
        valuePhone = findViewById(R.id.valuePhone)
        saveButton = findViewById(R.id.saveButton)

        loadUserData()

        saveButton.setOnClickListener {
            Toast.makeText(this, "Funcionalidade de salvar em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserData() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        valueName.text = document.getString("name") ?: "N/A"
                        valueEmail.text = document.getString("email") ?: "N/A"
                        valuePhone.text = document.getString("phone") ?: "N/A"
                    } else {
                        Toast.makeText(this, "Dados não encontrados", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}
