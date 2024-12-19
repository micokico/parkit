package com.example.parkit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MyCarAdicionarCarroActivity : AppCompatActivity() {
    private lateinit var spinnerTipoVeiculo: Spinner
    private lateinit var etNomeVeiculo: EditText
    private lateinit var etMatricula: EditText
    private lateinit var btnEscolherImagem: Button
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycar_adicionar_carro)

        // Initialize views
        spinnerTipoVeiculo = findViewById(R.id.spinnerTipoVeiculo)
        etNomeVeiculo = findViewById(R.id.etNomeVeiculo)
        etMatricula = findViewById(R.id.etMatricula)
        btnEscolherImagem = findViewById(R.id.btnEscolherImagem)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Set up spinner
        val vehicleTypes = arrayOf("Carro", "Moto", "Van", "Scooter")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoVeiculo.adapter = adapter

        // Choose image button
        btnEscolherImagem.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        // Save button
        btnSalvar.setOnClickListener {
            saveVehicleToFirebase()
        }

        // Cancel button
        btnCancelar.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            Toast.makeText(this, "Imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveVehicleToFirebase() {
        val vehicleType = spinnerTipoVeiculo.selectedItem.toString()
        val vehicleName = etNomeVeiculo.text.toString()
        val vehiclePlate = etMatricula.text.toString()

        if (vehicleName.isEmpty() || vehiclePlate.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance().getReference("vehicles")
        val vehicleId = database.push().key ?: return

        if (selectedImageUri != null) {
            val storageReference = FirebaseStorage.getInstance().getReference("vehicle_images/$vehicleId.jpg")
            storageReference.putFile(selectedImageUri!!).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val vehicle = Vehicle(vehicleId, vehicleType, vehicleName, vehiclePlate, uri.toString())
                    database.child(vehicleId).setValue(vehicle).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Veículo adicionado com sucesso", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao salvar veículo", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show()
            }
        } else {
            val vehicle = Vehicle(vehicleId, vehicleType, vehicleName, vehiclePlate, null)
            database.child(vehicleId).setValue(vehicle).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Veículo adicionado com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao salvar veículo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    data class Vehicle(
        val id: String,
        val type: String,
        val name: String,
        val plate: String,
        val imageUrl: String?
    )
}
