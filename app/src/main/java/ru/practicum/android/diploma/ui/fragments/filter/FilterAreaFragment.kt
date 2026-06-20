package ru.practicum.android.diploma.ui.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.databinding.FragmentFilterAreaBinding
import ru.practicum.android.diploma.presentation.area.AreaUi
import ru.practicum.android.diploma.presentation.area.AreaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.area.AreaUiState

class FilterAreaFragment : Fragment() {
    //binding
    private var _binding: FragmentFilterAreaBinding? = null
    private val binding get() = _binding!!

    //viewModel
    private val viewModel by viewModel<AreaViewModel>()

    //список регионов
    private val areas = mutableListOf<AreaUi>()
    private val areasAdapter = AreaAdapter(areas)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //подпись на liveData
        viewModel.observeScreenState().observe(viewLifecycleOwner) {
            render(it)
        }

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

    //состояния экрана
    private fun render(state: AreaUiState) {
        when(state) {
            is AreaUiState.Initial -> {}
            is AreaUiState.Content -> showContent(state.areas)
            is AreaUiState.Empty -> showEmpty(state.message)
            is AreaUiState.Error -> showError(state.message)
        }
    }

    private fun showError(message: String) {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_error_area)
        binding.placeholderText.text = message

        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE

        binding.regionList.visibility = View.GONE
    }

    private fun showEmpty(message: String) {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_empty)
        binding.placeholderText.text = message

        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE

        binding.regionList.visibility = View.GONE
    }

    private fun showContent(newAreas: List<AreaUi>) {
        binding.regionList.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE

        areas.clear()
        areas.addAll(newAreas)
        areasAdapter.notifyDataSetChanged()
    }
}
