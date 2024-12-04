import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.parkit.R

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // Referências dos slots
        val slot3b = findViewById<ImageView>(R.id.slot3b)
        val slot4c = findViewById<ImageView>(R.id.slot4c)
        val slot6a = findViewById<ImageView>(R.id.slot6a)
        val slot7b = findViewById<ImageView>(R.id.slot7b)
        val slot8c = findViewById<ImageView>(R.id.slot8c)

        // Mapa para armazenar os estados dos slots
        val slotStates = mutableMapOf(
            R.id.slot3b to "free",
            R.id.slot4c to "free",
            R.id.slot6a to "occupied",
            R.id.slot7b to "selected",
            R.id.slot8c to "free"
        )

        // Função para alterar a cor de acordo com o estado
        fun updateSlotColor(slot: ImageView, state: String) {
            val color = when (state) {
                "free" -> R.color.green  // Slot livre
                "occupied" -> R.color.black  // Slot ocupado
                "selected" -> R.color.blue  // Slot selecionado
                else -> R.color.gray  // Estado desconhecido
            }
            slot.setColorFilter(ContextCompat.getColor(this, color))
        }

        // Atualiza as cores iniciais dos slots
        updateSlotColor(slot3b, slotStates[R.id.slot3b]!!)
        updateSlotColor(slot4c, slotStates[R.id.slot4c]!!)
        updateSlotColor(slot6a, slotStates[R.id.slot6a]!!)
        updateSlotColor(slot7b, slotStates[R.id.slot7b]!!)
        updateSlotColor(slot8c, slotStates[R.id.slot8c]!!)

        // Clique em cada slot para alterar o estado
        slot3b.setOnClickListener {
            slotStates[R.id.slot3b] = if (slotStates[R.id.slot3b] == "free") "selected" else "free"
            updateSlotColor(slot3b, slotStates[R.id.slot3b]!!)
        }

        slot4c.setOnClickListener {
            slotStates[R.id.slot4c] = if (slotStates[R.id.slot4c] == "free") "selected" else "free"
            updateSlotColor(slot4c, slotStates[R.id.slot4c]!!)
        }

        slot6a.setOnClickListener {
            Toast.makeText(this, "Este slot está ocupado!", Toast.LENGTH_SHORT).show()
        }

        slot7b.setOnClickListener {
            slotStates[R.id.slot7b] = if (slotStates[R.id.slot7b] == "selected") "free" else "selected"
            updateSlotColor(slot7b, slotStates[R.id.slot7b]!!)
        }

        slot8c.setOnClickListener {
            slotStates[R.id.slot8c] = if (slotStates[R.id.slot8c] == "free") "selected" else "free"
            updateSlotColor(slot8c, slotStates[R.id.slot8c]!!)
        }
    }
}
