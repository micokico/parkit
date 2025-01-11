import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkit.R


class ParkingAdapter(
    private val slots: List<String>,
    private val onSlotClick: (String) -> Unit
) : RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder>() {

    class ParkingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSlotNumber: TextView = itemView.findViewById(R.id.tvSlotNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parking_slot, parent, false)
        return ParkingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingViewHolder, position: Int) {
        val slot = slots[position]
        holder.tvSlotNumber.text = slot

        holder.itemView.setOnClickListener {
            onSlotClick(slot)
        }
    }

    override fun getItemCount(): Int = slots.size
}
