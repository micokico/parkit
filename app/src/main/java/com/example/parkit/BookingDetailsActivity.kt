package com.example.parkit

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var parkingNameText: TextView
    private lateinit var dateText: TextView
    private lateinit var spotIdText: TextView
    private lateinit var totalCostText: TextView
    private lateinit var vehicleText: TextView
    private lateinit var vehicleTypeText: TextView
    private lateinit var backButton: ImageButton
    private lateinit var qrCodeImageView: ImageView
    private lateinit var uniqueIdText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        // Inicializar os componentes
        parkingNameText = findViewById(R.id.parkingNameText)
        dateText = findViewById(R.id.dateText)
        spotIdText = findViewById(R.id.spotIdText)
        totalCostText = findViewById(R.id.totalCostText)
        vehicleText = findViewById(R.id.vehicleText)
        vehicleTypeText = findViewById(R.id.vehicleTypeText)
        backButton = findViewById(R.id.btn_back)
        qrCodeImageView = findViewById(R.id.qrCodeImageView)
        uniqueIdText = findViewById(R.id.tvUniqueID)

        // Configurar o botão de voltar
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Recuperar os dados da Intent
        val parkingName = intent.getStringExtra("PARKING_NAME") ?: "Desconhecido"
        val date = intent.getStringExtra("DATE") ?: "Desconhecida"
        val spotId = intent.getStringExtra("SPOT_ID") ?: "Desconhecido"
        val totalCost = intent.getIntExtra("TOTAL_COST", 0)
        val vehicle = intent.getStringExtra("VEHICLE") ?: "Desconhecido"
        val vehicleType = intent.getStringExtra("VEHICLE_TYPE") ?: "Desconhecido"

        // Gerar código único
        val uniqueID = "RES-${System.currentTimeMillis() % 100000}"

        // Exibir os dados
        parkingNameText.text = parkingName
        dateText.text = date
        spotIdText.text = spotId
        totalCostText.text = "€$totalCost"
        vehicleText.text = vehicle
        vehicleTypeText.text = vehicleType
        uniqueIdText.text = "ID Único: $uniqueID" // Exibir o código único

        // Gerar código QR
        generateQRCode(uniqueID)
    }

    private fun generateQRCode(data: String) {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }

            qrCodeImageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}
