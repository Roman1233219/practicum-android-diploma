package ru.practicum.android.diploma.ui.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountrySelectionBinding
import ru.practicum.android.diploma.presentation.`filter-area`.AreaUi
import ru.practicum.android.diploma.presentation.`filter-area`.CountryUiState
import ru.practicum.android.diploma.presentation.`filter-area`.CountryViewModel

class CountrySelectionFragment : Fragment() {
    private var _binding: FragmentCountrySelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<CountryViewModel>()

    private var adapter: AreaAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCountrySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun initRecyclerView() {
        adapter = AreaAdapter(emptyList()) { country ->
            selectCountry(country)
        }
        binding.countryList.layoutManager = LinearLayoutManager(requireContext())
        binding.countryList.adapter = adapter
    }

    private fun selectCountry(country: AreaUi) {
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(FilterAreaFragment.COUNTRY_DATA_KEY, country)
        findNavController().navigateUp()
    }

    private fun render(state: CountryUiState) {
        when (state) {
            is CountryUiState.Loading -> {
                binding.progressBar.isVisible = true
                binding.countryList.isVisible = false
                binding.errorContainer.isVisible = false
            }
            is CountryUiState.Content -> {
                binding.progressBar.isVisible = false
                binding.countryList.isVisible = true
                binding.errorContainer.isVisible = false
                updateList(state.countries)
            }
            is CountryUiState.Error -> {
                binding.progressBar.isVisible = false
                binding.countryList.isVisible = false
                binding.errorContainer.isVisible = true
                binding.errorText.text = getString(state.messageRes)
            }
        }
    }

    private fun updateList(countries: List<AreaUi>) {
        adapter = AreaAdapter(countries) { country ->
            selectCountry(country)
        }
        binding.countryList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
