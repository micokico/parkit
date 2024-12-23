package com.example.parkit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MyCarAdicionarCarroActivity : AppCompatActivity() {
    private lateinit var spinnerTipoVeiculo: Spinner
    private lateinit var etNomeVeiculo: EditText
    private lateinit var etMatricula: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button

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
            saveVehicleToFirebase()
        }

        // Cancel button
        btnCancelar.setOnClickListener {
            finish()
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

    data class Vehicle(
        val id: String,
        val type: String,
        val name: String,
        val plate: String,
        val imageUrl: String?
    )
}
