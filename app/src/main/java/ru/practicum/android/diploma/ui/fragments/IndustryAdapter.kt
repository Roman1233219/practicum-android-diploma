package ru.practicum.android.diploma.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.R

class IndustryAdapter(private val onItemClick: (Industry) -> Unit) :
    RecyclerView.Adapter<IndustryViewHolder>() {
    
    var industrys = listOf<Industry>()
    private var selectedPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndustryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_industry_view, parent, false)
        return IndustryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: IndustryViewHolder,
        position: Int
    ) {
        val industry = industrys[position]
        val isSelected = position == selectedPosition
        
        holder.bind(industry, isSelected) { isChecked ->
            if (isChecked) {
                val previousSelected = selectedPosition
                selectedPosition = holder.adapterPosition
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onItemClick(industry)
            } else if (selectedPosition == holder.adapterPosition) {
                selectedPosition = -1
                onItemClick(Industry("", "")) // Уведомляем о сбросе выбора
            }
        }
    }

    override fun getItemCount(): Int {
        return industrys.size
    }
    
    fun getSelectedIndustry(): Industry? {
        return if (selectedPosition != -1) industrys[selectedPosition] else null
    }
}
