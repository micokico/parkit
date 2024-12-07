package com.example.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        val nameField = findViewById<EditText>(getResId("nameField", "id"))
        val emailField = findViewById<EditText>(getResId("emailField", "id"))
        val phoneField = findViewById<EditText>(getResId("phoneField", "id"))
        val passwordField = findViewById<EditText>(getResId("passwordField", "id"))
        val saveButton = findViewById<Button>(getResId("saveButton", "id"))

        saveButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val phone = phoneField.text.toString()
            val password = passwordField.text.toString()
            Toast.makeText(this, "Salvo:\n$name\n$email\n$phone\n$password", Toast.LENGTH_LONG).show()
        }
    }

    private fun getLayoutId(): Int {
        return resources.getIdentifier("activity_profile", "layout", packageName)
    }

    private fun getResId(resourceName: String, resourceType: String): Int {
        return resources.getIdentifier(resourceName, resourceType, packageName)
    }
}
