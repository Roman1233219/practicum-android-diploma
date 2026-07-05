package ru.practicum.android.diploma.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Area

class CountryAdapter(
    private val onItemClick: (Area) -> Unit
) : ListAdapter<Area, CountryAdapter.CountryVH>(CountryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryVH(view)
    }

    override fun onBindViewHolder(holder: CountryVH, position: Int) {
        val country = getItem(position)
        holder.bind(country, onItemClick)
    }

    inner class CountryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCountryName: TextView = itemView.findViewById(R.id.tvCountry)
        private val ivArrow: ImageView = itemView.findViewById(R.id.ivArrowForward)

        fun bind(country: Area, onItemClick: (Area) -> Unit) {
            tvCountryName.text = country.name

            itemView.setOnClickListener {
                onItemClick(country)
            }
        }
    }

    private class CountryDiffCallback : DiffUtil.ItemCallback<Area>() {
        override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem == newItem
        }
    }
}
