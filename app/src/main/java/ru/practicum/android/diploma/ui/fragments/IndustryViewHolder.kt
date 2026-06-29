package ru.practicum.android.diploma.ui.fragments

import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry

class IndustryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val radioButton: RadioButton = itemView.findViewById(R.id.name)

    fun bind(model: Industry, isSelected: Boolean, onCheckedChangeListener: (Boolean) -> Unit) {
        radioButton.text = model.industryName

        // Снимаем слушатель перед установкой значения, чтобы не зациклиться
        radioButton.setOnCheckedChangeListener(null)
        radioButton.isChecked = isSelected

        radioButton.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChangeListener(isChecked)
        }

        // Клик по самому айтему тоже должен менять состояние
        itemView.setOnClickListener {
            radioButton.isChecked = !radioButton.isChecked
        }
    }
}
