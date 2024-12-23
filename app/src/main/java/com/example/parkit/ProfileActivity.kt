package com.example.parkit

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var valueName: TextView
    private lateinit var valueEmail: TextView
    private lateinit var valuePhone: TextView
    private lateinit var saveButton: Button

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImage = findViewById(R.id.profileImage)
        valueName = findViewById(R.id.valueName)
        valueEmail = findViewById(R.id.valueEmail)
        valuePhone = findViewById(R.id.valuePhone)
        saveButton = findViewById(R.id.saveButton)

        loadUserData()

        saveButton.setOnClickListener {
            saveUserData()
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadUserData() {
        val phoneNumber = "918235917"
        firestore.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    valueName.text = document.getString("name") ?: "N/A"
                    valueEmail.text = document.getString("email") ?: "N/A"
                    valuePhone.text = document.getString("phoneNumber") ?: "N/A"
                } else {
                    Toast.makeText(this, "Dados não encontrados", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserData() {
        val phoneNumber = "918235917"

        val updatedData = hashMapOf(
            "name" to valueName.text.toString(),
            "email" to valueEmail.text.toString(),
            "phoneNumber" to valuePhone.text.toString()
        )

        firestore.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id
                    firestore.collection("users").document(documentId)
                        .update(updatedData as Map<String, Any>)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao acessar Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
