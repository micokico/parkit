package com.example.parkit

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView // Import da classe ImageView adicionado
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SpaceBookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_booking)

        val tvDurationCost = findViewById<TextView>(R.id.tvDurationCost)
        val seekBarDuration = findViewById<SeekBar>(R.id.seekBarDuration)
        val tvCheckInTime = findViewById<TextView>(R.id.tvCheckInTime)
        val ivEditCheckInTime = findViewById<ImageView>(R.id.ivEditCheckInTime)
        val switchDisabledParking = findViewById<Switch>(R.id.switchDisabledParking)
        val switchSpecialGuard = findViewById<Switch>(R.id.switchSpecialGuard)
        val btnBookSpace = findViewById<Button>(R.id.btnBookSpace)

        // Configurar o SeekBar
        seekBarDuration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val cost = progress * 200
                tvDurationCost.text = "$progress hours - N$cost"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Ação ao clicar no botão "Book Space"
        btnBookSpace.setOnClickListener {
            Toast.makeText(
                this,
                "Space booked successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Editar o horário de check-in
        ivEditCheckInTime.setOnClickListener {
            Toast.makeText(this, "Edit Check-in Time clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
