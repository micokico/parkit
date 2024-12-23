package com.example.parkit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MyCarAdicionarCarroActivity : AppCompatActivity() {
    private lateinit var spinnerTipoVeiculo: Spinner
    private lateinit var etNomeVeiculo: EditText
    private lateinit var etMatricula: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycar_adicionar_carro)

        // Initialize views
        spinnerTipoVeiculo = findViewById(R.id.spinnerTipoVeiculo)
        etNomeVeiculo = findViewById(R.id.etNomeVeiculo)
        etMatricula = findViewById(R.id.etMatricula)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Set up spinner
        val vehicleTypes = arrayOf("Carro", "Moto", "Van", "Scooter")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoVeiculo.adapter = adapter

        // Save button
        btnSalvar.setOnClickListener {
            saveVehicleToFirestore()
        }

        // Cancel button
        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun saveVehicleToFirestore() {
        val vehicleType = spinnerTipoVeiculo.selectedItem.toString()
        val vehicleName = etNomeVeiculo.text.toString()
        val vehiclePlate = etMatricula.text.toString()

        if (vehicleName.isEmpty() || vehiclePlate.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val metadataRef = firestore.collection("metadata").document("counters")
        val vehicleCollection = firestore.collection("Vehicle")

        firestore.runTransaction { transaction ->
            // Obter o contador atual
            val snapshot = transaction.get(metadataRef)
            val currentCounter = snapshot.getLong("VehicleCounter") ?: 1

            // Incrementar o contador
            transaction.update(metadataRef, "VehicleCounter", currentCounter + 1)

            // Criar o veículo com o novo ID
            val vehicleId = currentCounter.toString()
            val vehicle = hashMapOf(
                "id" to vehicleId,
                "type" to vehicleType,
                "name" to vehicleName,
                "plate" to vehiclePlate,
                "imageUrl" to null
            )

            // Salvar o veículo na coleção
            transaction.set(vehicleCollection.document(vehicleId), vehicle)
        }.addOnSuccessListener {
            Toast.makeText(this, "Veículo adicionado com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao salvar veículo", Toast.LENGTH_SHORT).show()
        }
    }
}
