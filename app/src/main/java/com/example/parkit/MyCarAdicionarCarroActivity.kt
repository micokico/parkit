package com.example.parkit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MyCarAdicionarCarroActivity : AppCompatActivity() {

    private lateinit var etNomeCarro: EditText
    private lateinit var etMatricula: EditText
    private lateinit var btnEscolherImagem: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnSalvar: Button

    private var selectedImageUri: String? = null  // Para armazenar URI da imagem

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycar_adicionar_carro)

        // Inicializar os componentes
        etNomeCarro = findViewById(R.id.etNomeCarro)
        etMatricula = findViewById(R.id.etMatricula)
        btnEscolherImagem = findViewById(R.id.btnEscolherImagem)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnSalvar = findViewById(R.id.btnSalvar)

        // Escolher imagem do dispositivo
        btnEscolherImagem.setOnClickListener {
            openImagePicker()
        }

        // Botão de Cancelar
        btnCancelar.setOnClickListener {
            finish() // Fecha a activity
        }

        // Botão de Salvar
        btnSalvar.setOnClickListener {
            saveCarDetails()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Escolha uma imagem"), IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data.toString()
            Toast.makeText(this, "Imagem selecionada com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveCarDetails() {
        val nomeCarro = etNomeCarro.text.toString().trim()
        val matricula = etMatricula.text.toString().trim()

        if (nomeCarro.isEmpty()) {
            Toast.makeText(this, "Por favor, insira o nome do carro", Toast.LENGTH_SHORT).show()
            return
        }
        if (matricula.isEmpty()) {
            Toast.makeText(this, "Por favor, insira a matrícula do carro", Toast.LENGTH_SHORT).show()
            return
        }

        // Simula o salvamento dos dados
        Toast.makeText(this, "Carro salvo:\nNome: $nomeCarro\nMatrícula: $matricula", Toast.LENGTH_LONG).show()

        // Fechar a Activity
        finish()
    }
}
