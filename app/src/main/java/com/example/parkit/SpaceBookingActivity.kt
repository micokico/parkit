package com.example.parkit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SpaceBookingActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private var selectedSpot: String? = null
    private var currentFloor: Int = 1 // Valor padrão
    private var pricePerHour: Double = 0.0 // Preço por hora
    private var selectedDate: String? = null // Data selecionada
    private var selectedTime: String? = null // Hora selecionada
    private var vehicleType: String? = null // Tipo do veículo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_booking)

        // Recebendo os dados da Intent
        selectedSpot = intent.getStringExtra("SELECTED_SPOT")
        val parkingName = intent.getStringExtra("PARKING_NAME")
        vehicleType = intent.getStringExtra("VEHICLE_TYPE")
        val parkingPrice = intent.getDoubleExtra("PARKING_PRICE", 0.0) // Recebe como Double
        pricePerHour = parkingPrice // Define o preço por hora
        currentFloor = intent.getIntExtra("CURRENT_FLOOR", 1)

        // Configura os TextViews
        findViewById<TextView>(R.id.tvSelectedSpace).text = "Espaço: $selectedSpot"
        findViewById<TextView>(R.id.tvParkingName).text = "Nome do Estacionamento: $parkingName"
        findViewById<TextView>(R.id.tvParkingPrice).text = "Preço por hora: $pricePerHour €"
        findViewById<TextView>(R.id.tvVehicleType).text = "Veículo: $vehicleType"

        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)
        val tvSelectedTime = findViewById<TextView>(R.id.tvSelectedTime)

        // Configura o SeekBar
        val seekBar = findViewById<SeekBar>(R.id.seekBarDuration)
        val tvDurationCost = findViewById<TextView>(R.id.tvDurationCost)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val hours = if (progress == 0) 1 else progress
                val totalCost = hours * pricePerHour
                tvDurationCost.text = "$hours horas - $totalCost €"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Botão para selecionar data
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        btnPickDate.setOnClickListener {
            showDatePicker(tvSelectedDate)
        }

        // Botão para selecionar hora
        val btnPickTime = findViewById<Button>(R.id.btnPickTime)
        btnPickTime.setOnClickListener {
            showTimePicker(tvSelectedTime)
        }

        // Botão de reserva
        val bookButton = findViewById<Button>(R.id.btnBookSpace)
        bookButton.setOnClickListener {
            val duration = seekBar.progress.coerceAtLeast(1)
            val totalCost = duration * pricePerHour

            if (selectedDate == null || selectedTime == null) {
                Toast.makeText(this, "Por favor, selecione a data e a hora.", Toast.LENGTH_SHORT).show()
            } else {
                saveReservationToFirestore(
                    selectedSpot!!,
                    parkingName,
                    vehicleType,
                    totalCost,
                    duration,
                    selectedDate!!,
                    selectedTime!!
                )
            }
        }
    }

    /**
     * Exibe o DatePickerDialog para selecionar uma data
     */
    private fun showDatePicker(tvSelectedDate: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            tvSelectedDate.text = "Data Selecionada: $selectedDate"
        }, year, month, day)

        datePickerDialog.show()
    }

    /**
     * Exibe o TimePickerDialog para selecionar uma hora
     */
    private fun showTimePicker(tvSelectedTime: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            tvSelectedTime.text = "Hora Selecionada: $selectedTime"
        }, hour, minute, true)

        timePickerDialog.show()
    }

    /**
     * Salvar a reserva no Firestore
     */
    private fun saveReservationToFirestore(
        spotId: String,
        parkingName: String?,
        vehicleType: String?,
        totalCost: Double,
        duration: Int,
        date: String,
        time: String
    ) {
        val reservationData = mapOf(
            "spotId" to spotId,
            "parkingName" to parkingName,
            "vehicleType" to vehicleType,
            "totalCost" to totalCost,
            "duration" to duration,
            "date" to date,
            "time" to time,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("reservations")
            .add(reservationData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Reserva salva com sucesso!", Toast.LENGTH_SHORT).show()
                navigateToPayment(documentReference.id, totalCost)
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao salvar reserva: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToPayment(reservationId: String, totalCost: Double) {
        val intent = Intent(this, RealPaymentActivity::class.java)
        intent.putExtra("RESERVATION_ID", reservationId)
        intent.putExtra("TOTAL_COST", totalCost)
        startActivity(intent)
        finish() // Finaliza esta atividade para evitar voltar
    }


    /**
     * Atualizar o estado do espaço de estacionamento no Firestore
     */
    private fun updateParkingSpotInFirestore(spotId: String) {
        val floorKey = "floor_$currentFloor"
        firestore.collection("parkingSpots").document(floorKey)
            .update(spotId, "car")
            .addOnSuccessListener {
                Toast.makeText(this, "Espaço atualizado como reservado.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao atualizar o espaço: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
