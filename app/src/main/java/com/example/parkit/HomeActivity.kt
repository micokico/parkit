package com.example.parkit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class HomeActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var database: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load configuration for OSM
        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))
        setContentView(R.layout.activity_home)

        // Initialize MapView
        mapView = findViewById(R.id.map)
        mapView.setMultiTouchControls(true)

        // Set bounding box for Porto region
        val portoBoundingBox = BoundingBox(41.366, -7.895, 40.801, -8.847)
        mapView.setScrollableAreaLimitDouble(portoBoundingBox)

        // Set initial map view
        val portoCenter = GeoPoint(41.14961, -8.61099) // Coordenadas do Porto
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(portoCenter)


        // Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("parks/carParks")

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            loadCarParks()
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = GeoPoint(location.latitude, location.longitude)
                val userMarker = Marker(mapView)
                userMarker.position = userLocation
                userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                userMarker.title = "Minha Localização"
                userMarker.icon = resources.getDrawable(R.drawable.ic_location, null)
                mapView.overlays.add(userMarker)

                // Centralizar o mapa na posição do usuário
                mapView.controller.setZoom(15.0)
                mapView.controller.setCenter(userLocation)
            } else {
                // Fallback para o Porto
                val portoCenter = GeoPoint(41.14961, -8.61099) // Coordenadas do Porto
                mapView.controller.setZoom(15.0)
                mapView.controller.setCenter(portoCenter)
                Toast.makeText(this, "Localização não encontrada. Centralizando no Porto.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            // Fallback para o Porto em caso de erro
            val portoCenter = GeoPoint(41.14961, -8.61099) // Coordenadas do Porto
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(portoCenter)
            Toast.makeText(this, "Erro ao obter localização: ${it.message}. Centralizando no Porto.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadCarParks() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Remova apenas os marcadores de parques, mantendo o marcador do usuário
                val existingMarkers = mapView.overlays.filterIsInstance<Marker>()
                mapView.overlays.removeAll(existingMarkers.filter { it.title != "Minha Localização" })

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
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Erro ao carregar dados: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
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
