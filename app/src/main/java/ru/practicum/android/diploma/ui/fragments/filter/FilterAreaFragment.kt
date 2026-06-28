package ru.practicum.android.diploma.ui.fragments.filter

import android.R.attr.colorControlNormal
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.bundle.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterAreaBinding
import ru.practicum.android.diploma.presentation.`filter-area`.AreaUi
import ru.practicum.android.diploma.presentation.`filter-area`.AreaUiState
import ru.practicum.android.diploma.presentation.`filter-area`.AreaViewModel
import ru.practicum.android.diploma.presentation.`filter-area`.RegionUiState
import ru.practicum.android.diploma.presentation.`filter-area`.RegionViewModel
import kotlin.getValue

class FilterAreaFragment : Fragment() {
    //binding
    private var _binding: FragmentFilterAreaBinding? = null
    private val binding get() = _binding!!

    //viewModel
    private val viewModel by viewModel<AreaViewModel>()

    // свойства для хранения текущего состояния отображения
    private var isCountrySelected = false
    private var isRegionSelected = false

    private var pendingCountry: AreaUi? = null
    private var pendingRegion: AreaUi? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // подпись на liveData
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        // Установка кнопки "Назад"
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // кнопки выбора объекта или очистки выбора
        binding.selectCountryButton.setOnClickListener {
            if (!isCountrySelected) {
                findNavController().navigate(R.id.action_workPlaceSelectionFragment_to_countrySelectionFragment)
            } else {
                viewModel.selectCountry(null)
            }
        }

        binding.selectRegionButton.setOnClickListener {
            if (!isRegionSelected) {
                val countryId = when (val state = viewModel.observeState().value) {
                    is AreaUiState.Content -> state.country?.areaId ?: -1
                    else -> -1
                }

                findNavController().navigate(
                    R.id.action_workPlaceSelectionFragment_to_regionSelectionFragment,
                    bundleOf(
                        "countryId" to countryId
                    )
                )
            } else {
                viewModel.selectRegion(null)
            }
        }

        // кнопка Выбрать
        binding.selectButton.setOnClickListener {

            findNavController().navigateUp()
        }

        // получение данных из других фрагментов
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle

        // слушаем возврат страны
        savedStateHandle
            ?.getLiveData<AreaUi>(COUNTRY_DATA_KEY)
            ?.observe(viewLifecycleOwner) { country ->
                pendingCountry = country

                if (pendingRegion == null) {
                    viewModel.selectCountry(country)
                } else {
                    viewModel.selectLocation(
                        pendingCountry,
                        pendingRegion
                    )

                    pendingCountry = null
                    pendingRegion = null
                }

                savedStateHandle.remove<AreaUi>(COUNTRY_DATA_KEY)
            }

        // слушаем возврат региона
        savedStateHandle
            ?.getLiveData<AreaUi>(REGION_DATA_KEY)
            ?.observe(viewLifecycleOwner) { region ->
                pendingRegion = region

                if (pendingCountry == null) {
                    viewModel.selectRegion(region)
                } else {
                    viewModel.selectLocation(
                        pendingCountry,
                        pendingRegion
                    )

                    pendingCountry = null
                    pendingRegion = null
                }

                savedStateHandle.remove<AreaUi>(REGION_DATA_KEY)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // состояния экранов
    private fun render(state: AreaUiState) {
        when (state) {
            is AreaUiState.Empty -> {
                isCountrySelected = false
                isRegionSelected = false

                renderCountryView(null)
                renderRegionView(null)
                binding.selectButton.visibility = View.GONE
            }
            is AreaUiState.Content -> {
                isCountrySelected = state.country != null
                isRegionSelected = state.region != null

                renderCountryView(state.country)
                renderRegionView(state.region)
                binding.selectButton.visibility = View.VISIBLE
            }
        }
    }

    private fun renderCountryView(country: AreaUi?) {
        if (country != null) {
            binding.countryHint.visibility = View.VISIBLE
            binding.countryText.text = country.areaName
            binding.countryText.setTextColor(getColorFromAttr(com.google.android.material.R.attr.colorOnSurface))
            binding.countryActionIcon.setImageResource(R.drawable.ic_close_24)
        } else {
            binding.countryHint.visibility = View.GONE
            binding.countryText.text = getString(R.string.country)
            binding.countryText.setTextColor(getColorFromAttr(colorControlNormal))
            binding.countryActionIcon.setImageResource(R.drawable.ic_arrow_forward_24)
        }
    }

    private fun renderRegionView(region: AreaUi?) {
        if (region != null) {
            binding.regionHint.visibility = View.VISIBLE
            binding.regionText.text = region.areaName
            binding.regionText.setTextColor(getColorFromAttr(com.google.android.material.R.attr.colorOnSurface))
            binding.regionActionIcon.setImageResource(R.drawable.ic_close_24)
        } else {
            binding.regionHint.visibility = View.GONE
            binding.regionText.text = getString(R.string.region)
            binding.regionText.setTextColor(getColorFromAttr((colorControlNormal)))
            binding.regionActionIcon.setImageResource(R.drawable.ic_arrow_forward_24)
        }
    }

    private fun getColorFromAttr(@AttrRes attrColor: Int): Int {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(attrColor, typedValue, true)
        return typedValue.data
    }

    companion object {
        const val COUNTRY_DATA_KEY = "selected_country_data"
        const val REGION_DATA_KEY = "selected_region_data"
    }
}

