package ru.practicum.android.diploma.ui.fragments.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R

class AreaAdapter(
    private val areas: List<String>
) : RecyclerView.Adapter<AreaAdapter.AreasViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return AreasViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AreasViewHolder,
        position: Int
    ) {
        holder.bind(areas[position])
    }

    override fun getItemCount(): Int {
        return areas.size
    }

    inner class AreasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val areaName: TextView = itemView.findViewById(R.id.locationName)

        fun bind(model: String) {
            areaName.text = model
        }
    }

}
