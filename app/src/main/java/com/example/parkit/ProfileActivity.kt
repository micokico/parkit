package com.example.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId("activity_profile"))

        profileImage = findViewById(getResId("profileImage", "id"))
        val nameField = findViewById<EditText>(getResId("nameField", "id"))
        val emailField = findViewById<EditText>(getResId("emailField", "id"))
        val phoneField = findViewById<EditText>(getResId("phoneField", "id"))
        val passwordField = findViewById<EditText>(getResId("passwordField", "id"))
        val saveButton = findViewById<Button>(getResId("saveButton", "id"))

        // Ação ao clicar na imagem de perfil
        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Ação ao salvar perfil
        saveButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val phone = phoneField.text.toString()
            val password = passwordField.text.toString()

            // Verificar se uma imagem foi selecionada antes de fazer o upload
            if (imageUri != null) {
                uploadImageToFirebase(imageUri!!) { imageUrl ->
                    // Mostra uma mensagem de sucesso junto com o URL da imagem
                    Toast.makeText(this, "Imagem salva em $imageUrl", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Nenhuma imagem foi selecionada", Toast.LENGTH_SHORT).show()
            }

            // Exibir os valores inseridos (nome, email, telefone e senha)
            Toast.makeText(this, "Salvo:\n$name\n$email\n$phone\n$password", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                imageUri = it // Armazenar a URI da imagem selecionada
                profileImage.setImageURI(it) // Exibir a imagem no ImageView
            }
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri, onSuccess: (String) -> Unit) {
        // Nome único para o arquivo no Firebase Storage
        val fileName = "profile_images/${UUID.randomUUID()}.jpg"
        val fileRef = storageReference.child(fileName)

        // Fazer o upload da imagem para o Firebase Storage
        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    // Retorna a URL pública da imagem
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro no upload: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getResId(resourceName: String, resourceType: String): Int {
        return resources.getIdentifier(resourceName, resourceType, packageName)
    }

    private fun getLayoutId(layoutName: String): Int {
        return resources.getIdentifier(layoutName, "layout", packageName)
    }
}
