package ru.practicum.android.diploma.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R

class CountryAdapter(
    private var countries: List<String>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryVH =
        CountryVH(LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false))

    override fun onBindViewHolder(holder: CountryVH, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

    inner class CountryVH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvCountry = view.findViewById<TextView>(R.id.tvCountry)

        init {
            view.setOnClickListener {
                clickListener.invoke(countries[adapterPosition])
            }
        }

        fun bind(countryName: String) {
            tvCountry.text = countryName
        }
    }
}
