package ru.practicum.android.diploma.ui.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.databinding.FragmentFilterAreaBinding
import ru.practicum.android.diploma.presentation.area.AreaUi
import ru.practicum.android.diploma.util.debounce

class FilterAreaFragment : Fragment() {
    //binding
    private var _binding: FragmentFilterAreaBinding? = null
    private val binding get() = _binding!!

    //список регионов
    private val areas = mutableListOf<AreaUi>()
    private val areasAdapter = AreaAdapter(areas)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Установка кнопки "Назад"
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        //список регионов
        binding.regionList.layoutManager = LinearLayoutManager(requireContext())
        binding.regionList.adapter = areasAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
