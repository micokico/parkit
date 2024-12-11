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
        setContentView(findViewById(getResId("activity_profile", "layout")))

        profileImage = findViewById(getResId("profileImage", "id"))
        val nameField = findViewById<EditText>(getResId("nameField", "id"))
        val emailField = findViewById<EditText>(getResId("emailField", "id"))
        val phoneField = findViewById<EditText>(getResId("phoneField", "id"))
        val passwordField = findViewById<EditText>(getResId("passwordField", "id"))
        val saveButton = findViewById<Button>(getResId("saveButton", "id"))

        // Configura o clique na imagem para escolher uma foto
        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Configura o botão de salvar
        saveButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val phone = phoneField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                uploadImageToFirebase(imageUri!!) { imageUrl ->
                    Toast.makeText(this, "Imagem salva em $imageUrl", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Nenhuma imagem foi selecionada", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(
                this,
                "Dados salvos:\nNome: $name\nEmail: $email\nTelefone: $phone\nSenha: $password",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                imageUri = it
                profileImage.setImageURI(it)
            }
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri, onSuccess: (String) -> Unit) {
        val fileName = "profile_images/${UUID.randomUUID()}.jpg"
        val fileRef = storageReference.child(fileName)

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
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
}
