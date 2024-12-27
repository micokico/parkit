package com.example.parkit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_booking)

        // Recebendo os dados passados pela ChooseSpaceActivity
        selectedSpot = intent.getStringExtra("SELECTED_SPOT")
        val parkingName = intent.getStringExtra("PARKING_NAME")
        val vehicleType = intent.getStringExtra("VEHICLE_TYPE")
        val parkingPrice = intent.getStringExtra("PARKING_PRICE")?.toDoubleOrNull() ?: 0.0
        pricePerHour = parkingPrice // Define o preço por hora
        currentFloor = intent.getIntExtra("CURRENT_FLOOR", 1)

        // Atualizando os TextViews com os dados recebidos
        findViewById<TextView>(R.id.tvSelectedSpace).text = "Espaço: $selectedSpot"
        findViewById<TextView>(R.id.tvParkingName).text = "Nome do Estacionamento: $parkingName"
        findViewById<TextView>(R.id.tvParkingPrice).text = "Preço por hora: $pricePerHour €"

        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate) // Para exibir a data
        val tvSelectedTime = findViewById<TextView>(R.id.tvSelectedTime) // Para exibir o horário

        // Configurando o SeekBar para estimar o tempo e calcular o custo
        val seekBar = findViewById<SeekBar>(R.id.seekBarDuration)
        val tvDurationCost = findViewById<TextView>(R.id.tvDurationCost)

        // Listener para o SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val hours = if (progress == 0) 1 else progress // Pelo menos 1 hora
                val totalCost = hours * pricePerHour
                tvDurationCost.text = "$hours horas - $totalCost €"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Configurando o botão para abrir o calendário
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        btnPickDate.setOnClickListener {
            showDatePicker(tvSelectedDate)
        }

        // Configurando o botão para abrir o seletor de horário
        val btnPickTime = findViewById<Button>(R.id.btnPickTime)
        btnPickTime.setOnClickListener {
            showTimePicker(tvSelectedTime)
        }

        // Configurando o botão de reserva
        val bookButton = findViewById<Button>(R.id.btnBookSpace)
        bookButton.setOnClickListener {
            if (selectedSpot != null) {
                val duration = seekBar.progress.coerceAtLeast(1) // Certifica-se de que o mínimo é 1 hora
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
            } else {
                Toast.makeText(this, "Erro ao recuperar espaço selecionado!", Toast.LENGTH_SHORT).show()
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
            // Formata a data selecionada
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
            // Formata a hora selecionada
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
            "date" to date, // Adiciona a data selecionada
            "time" to time, // Adiciona o horário selecionado
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("reservations")
            .add(reservationData)
            .addOnSuccessListener {
                Toast.makeText(this, "Reserva concluída com sucesso!", Toast.LENGTH_SHORT).show()
                updateParkingSpotInFirestore(spotId)
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao salvar reserva: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Atualizar o estado do espaço de estacionamento no Firestore
     */
    private fun updateParkingSpotInFirestore(spotId: String) {
        val floorKey = "floor_$currentFloor"
        firestore.collection("parkingSpots").document(floorKey)
            .update(spotId, "car") // Define o estado do espaço como "ocupado"
            .addOnSuccessListener {
                Toast.makeText(this, "Espaço atualizado como reservado.", Toast.LENGTH_SHORT).show()
                finish() // Finaliza a atividade após a atualização
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao atualizar o espaço: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
