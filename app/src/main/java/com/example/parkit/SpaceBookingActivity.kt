package com.example.parkit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SpaceBookingActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private var selectedSpot: String? = null
    private var currentFloor: Int = 1
    private var pricePerHour: Double = 0.0
    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private var vehicleType: String? = null
    private val vehiclePrices = mutableMapOf<String, Double>()
    private val userVehicles = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_booking)

        // Dados da Intent
        selectedSpot = intent.getStringExtra("SELECTED_SPOT")
        val parkingName = intent.getStringExtra("PARKING_NAME") ?: ""
        vehicleType = intent.getStringExtra("VEHICLE_TYPE")
        pricePerHour = intent.getDoubleExtra("PARKING_PRICE", 0.0)
        currentFloor = intent.getIntExtra("CURRENT_FLOOR", 1)

        // Configuração dos TextViews
        findViewById<TextView>(R.id.tvSelectedSpace).text = "Espaço: $selectedSpot"
        findViewById<TextView>(R.id.tvParkingName).text = "Nome do Estacionamento: $parkingName"
        findViewById<TextView>(R.id.tvParkingPrice).text = "Preço por hora: $pricePerHour €"
        val tvVehicleType = findViewById<TextView>(R.id.tvVehicleType)
        tvVehicleType.text = "Veículo: $vehicleType"

        val spinnerVehicle = findViewById<Spinner>(R.id.spinnerVehicle)
        loadVehiclesFromFirestore(spinnerVehicle, tvVehicleType)
        loadVehiclePricesFromFirestore()

        // Listener do Spinner
        spinnerVehicle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedVehicle = parent.getItemAtPosition(position).toString()
                if (selectedVehicle != "Selecione um veículo") {
                    updatePriceBasedOnVehicle(selectedVehicle)
                    tvVehicleType.text = "Veículo: $selectedVehicle"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Configuração do SeekBar
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
            showDatePicker(findViewById(R.id.tvSelectedDate))
        }

        // Botão para selecionar hora
        val btnPickTime = findViewById<Button>(R.id.btnPickTime)
        btnPickTime.setOnClickListener {
            showTimePicker(findViewById(R.id.tvSelectedTime))
        }

        // Botão de reserva
        val bookButton = findViewById<Button>(R.id.btnBookSpace)
        bookButton.setOnClickListener {
            val selectedVehicle = spinnerVehicle.selectedItem.toString()
            val duration = seekBar.progress.coerceAtLeast(1)
            val totalCost = duration * pricePerHour

            if (selectedDate == null || selectedTime == null) {
                Toast.makeText(this, "Por favor, selecione a data e a hora.", Toast.LENGTH_SHORT).show()
            } else if (selectedVehicle == "Selecione um veículo") {
                Toast.makeText(this, "Por favor, selecione um veículo.", Toast.LENGTH_SHORT).show()
            } else {
                saveReservationToFirestore(
                    selectedSpot!!,
                    parkingName,
                    selectedVehicle,
                    vehicleType,
                    totalCost,
                    duration,
                    selectedDate!!,
                    selectedTime!!
                )
            }
        }

        // Botão para voltar à página inicial
        val backButton = findViewById<Button>(R.id.btnBackToHome)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun updatePriceBasedOnVehicle(selectedVehicle: String) {
        val vehicleTypeKey = when {
            selectedVehicle.contains("Car", ignoreCase = true) -> "priceCar"
            selectedVehicle.contains("Bike", ignoreCase = true) -> "priceBike"
            selectedVehicle.contains("Van", ignoreCase = true) -> "priceVan"
            selectedVehicle.contains("Scooter", ignoreCase = true) -> "priceScooter"
            else -> null
        }

        vehicleTypeKey?.let {
            pricePerHour = vehiclePrices[it] ?: 0.0
            findViewById<TextView>(R.id.tvParkingPrice).text = "Preço por hora: $pricePerHour €"
        }
    }

    private fun loadVehiclesFromFirestore(spinner: Spinner, tvVehicleType: TextView) {
        firestore.collection("Vehicle")
            .get() // Carrega todos os documentos da coleção "Vehicle"
            .addOnSuccessListener { result ->
                userVehicles.clear()
                userVehicles.add("Selecione um veículo") // Opção padrão no spinner

                for (document in result) {
                    val vehicleName = document.getString("name") ?: "Desconhecido"
                    val plate = document.getString("plate") ?: "Sem placa"
                    val type = document.getString("type") ?: "Desconhecido"

                    // Adiciona ao spinner os dados formatados
                    userVehicles.add("$vehicleName - $plate ($type)")
                }

                // Configura o adapter para o spinner
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userVehicles)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao carregar veículos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadVehiclePricesFromFirestore() {
        firestore.collection("parkingPrices").document("parking1")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    vehiclePrices["priceCar"] = document.getDouble("priceCar") ?: 0.0
                    vehiclePrices["priceBike"] = document.getDouble("priceBike") ?: 0.0
                    vehiclePrices["priceVan"] = document.getDouble("priceVan") ?: 0.0
                    vehiclePrices["priceScooter"] = document.getDouble("priceScooter") ?: 0.0
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao carregar preços: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

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

    private fun saveReservationToFirestore(
        spotId: String,
        parkingName: String?,
        vehicle: String?,
        vehicleType: String?,
        totalCost: Double,
        duration: Int,
        date: String,
        time: String
    ) {
        val reservationData = mapOf(
            "spotId" to spotId,
            "parkingName" to parkingName,
            "vehicle" to vehicle,
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
        finish()
    }
}
