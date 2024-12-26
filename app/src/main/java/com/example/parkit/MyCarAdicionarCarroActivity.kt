package com.example.parkit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MyCarAdicionarCarroActivity : AppCompatActivity() {

    private lateinit var spinnerTipoVeiculo: Spinner
    private lateinit var etNomeVeiculo: EditText
    private lateinit var etMatricula: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnEscolherImagem: Button
    private var selectedImageUri: Uri? = null
    private val firestore = FirebaseFirestore.getInstance()

    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycar_adicionar_carro)

        // Inicializar as views
        spinnerTipoVeiculo = findViewById(R.id.spinnerTipoVeiculo)
        etNomeVeiculo = findViewById(R.id.etNomeVeiculo)
        etMatricula = findViewById(R.id.etMatricula)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnEscolherImagem = findViewById(R.id.btnEscolherImagem)

        val vehicleTypes = arrayOf("Carro", "Moto", "Van", "Scooter")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoVeiculo.adapter = adapter

        btnSalvar.setOnClickListener {
            saveVehicleToFirestore()
        }

        btnCancelar.setOnClickListener {
            finish()
        }

        // Botão Escolher Imagem
        btnEscolherImagem.setOnClickListener {
            openGallery()
        }
    }

    // Função para abrir a galeria de imagens
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            selectedImageUri = data?.data
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
                "imageUrl" to selectedImageUri?.toString() // Salvar a URL da imagem, se foi selecionada
            )

            // Salvar o veículo na coleção
            transaction.set(vehicleCollection.document(vehicleId), vehicle)
        }.addOnSuccessListener {
            Toast.makeText(this, "Veículo adicionado com sucesso", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK) // Retorna RESULT_OK para indicar sucesso
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao salvar veículo", Toast.LENGTH_SHORT).show()
        }
    }
}
