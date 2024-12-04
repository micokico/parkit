import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkit.R

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // Lista de slots disponíveis
        val parkingSlots = listOf("3B", "4C", "6A", "7B", "8C", "1A", "2B", "5D", "9E", "10F")

        // Configurando RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvParkingSlots)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 colunas
        recyclerView.adapter = ParkingAdapter(parkingSlots) { selectedSlot ->
            Toast.makeText(this, "Slot Selecionado: $selectedSlot", Toast.LENGTH_SHORT).show()
        }
    }
}
