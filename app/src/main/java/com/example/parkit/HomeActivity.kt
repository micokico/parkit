package com.example.parkit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class HomeActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar o osmdroid
        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))
        setContentView(R.layout.activity_home)

        // Inicializar o MapView
        mapView = findViewById(R.id.map)
        mapView.setMultiTouchControls(true)

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance().getReference("carParks")

        // Verificar permissões de localização
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            loadCarParks()
        }
    }

    // Método para carregar os dados dos parques do Firebase
    private fun loadCarParks() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Adicionar marcadores de cada parque
                for (carParkSnapshot in snapshot.children) {
                    val name = carParkSnapshot.child("name").getValue(String::class.java)
                    val latitude = carParkSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude = carParkSnapshot.child("longitude").getValue(Double::class.java)

                    if (latitude != null && longitude != null && name != null) {
                        val location = GeoPoint(latitude, longitude)
                        val marker = Marker(mapView)
                        marker.position = location
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = name
                        mapView.overlays.add(marker)
                    }
                }

                // Centralizar no primeiro ponto (se existir)
                if (snapshot.children.iterator().hasNext()) {
                    val firstChild = snapshot.children.iterator().next()
                    val latitude = firstChild.child("latitude").getValue(Double::class.java)
                    val longitude = firstChild.child("longitude").getValue(Double::class.java)
                    if (latitude != null && longitude != null) {
                        val firstLocation = GeoPoint(latitude, longitude)
                        mapView.controller.setZoom(14.0)
                        mapView.controller.setCenter(firstLocation)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Erro ao carregar dados: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Gerenciar permissão de localização
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCarParks()
        } else {
            Toast.makeText(this, "Permissão de localização necessária", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
