package ru.practicum.android.diploma.ui.fragments.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionSelectionBinding
import ru.practicum.android.diploma.presentation.`filter-area`.AreaUi
import ru.practicum.android.diploma.presentation.`filter-area`.RegionUiState
import ru.practicum.android.diploma.presentation.`filter-area`.RegionViewModel

class RegionSelectionFragment : Fragment() {
    // binding
    private var _binding: FragmentRegionSelectionBinding? = null
    private val binding get() = _binding!!

    // viewModel
    private val viewModel by viewModel<RegionViewModel>()

    // список регионов
    private val regions = mutableListOf<AreaUi>()
    private val regionsAdapter = AreaAdapter(regions) { area ->
        Log.d("getAreaId", area.areaId.toString())
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(FilterAreaFragment.REGION_DATA_KEY, area)

        findNavController().navigateUp()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegionSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // подпись на liveData
        viewModel.observeScreenState().observe(viewLifecycleOwner) {
            render(it)
        }

        // Подготовка экрана "Ничего не найдено" (для демонстрации/заглушки)
        binding.layoutNoFound.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_found)
        binding.layoutNoFound.tvPlaceholderText.text = getString(R.string.no_region)

        // Установка кнопки "Назад"
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // список регионов
        binding.regionList.layoutManager = LinearLayoutManager(requireContext())
        binding.regionList.adapter = regionsAdapter

        // поиск
        binding.regionSearchInput.setEndIconOnClickListener {
            val currentText = binding.regionSearchInput.editText?.text.toString()

            if (currentText.isNotEmpty()) {
                binding.regionSearchInput.editText?.text?.clear()
            } else {
                binding.regionSearchInput.editText?.requestFocus()
            }
        }

        binding.regionSearchInput.editText?.doOnTextChanged { text, _, _, _ ->
            val query = text.toString()

            binding.regionSearchInput.endIconDrawable =
                ContextCompat.getDrawable(
                    requireContext(),
                    if (query.isEmpty()) {
                        R.drawable.ic_search_24
                    } else {
                        R.drawable.ic_close_24
                    }
                )

            viewModel.searchDebounce(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // состояния экрана
    private fun render(state: RegionUiState) {
        when (state) {
            is RegionUiState.Initial -> {}
            is RegionUiState.Content -> showContent(state.regions)
            is RegionUiState.Empty -> showEmpty(state.messageRes)
            is RegionUiState.Error -> showError(state.messageRes)
        }
    }

    private fun showError(messageRes: Int) {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_error_area)
        binding.placeholderText.text = getString(messageRes)

        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE

        binding.regionList.visibility = View.GONE
    }

    private fun showEmpty(messageRes: Int) {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_empty)
        binding.placeholderText.text = getString(messageRes)

        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE

        binding.regionList.visibility = View.GONE
    }

    private fun showContent(newAreas: List<AreaUi>) {
        binding.regionList.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE

        regions.clear()
        regions.addAll(newAreas)
        regionsAdapter.notifyDataSetChanged()
    }

    companion object {
        const val AREA_DATA_KEY = "selected_area_id"
    }
}
