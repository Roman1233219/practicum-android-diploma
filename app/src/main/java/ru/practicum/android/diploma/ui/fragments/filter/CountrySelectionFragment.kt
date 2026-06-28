package ru.practicum.android.diploma.ui.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountrySelectionBinding
import ru.practicum.android.diploma.presentation.viewmodels.CountrySelectionViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FiltrationCountryState
import ru.practicum.android.diploma.ui.adapter.CountryAdapter

class CountrySelectionFragment : Fragment() {
    private var _binding: FragmentCountrySelectionBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CountrySelectionViewModel by viewModel()

    private var adapter: CountryAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCountrySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Установка кнопки "Назад"
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = CountryAdapter(onItemClick = { selectedCountry ->
            viewModel.onCountrySelected(selectedCountry)
            findNavController().navigate(R.id.action_filtersFragment_to_regionSelectionFragment)
        })
        binding.rvCountries.adapter = adapter
        binding.rvCountries.layoutManager = LinearLayoutManager(requireContext())

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is FiltrationCountryState.Loading -> {
                            //
                        }
                        is FiltrationCountryState.Success -> {

                            if (state.countries.isEmpty()) {
                                binding.rvCountries.isVisible = false
                            } else {
                                binding.rvCountries.isVisible = true
                                adapter?.submitList(state.countries)
                            }
                        }
                        is FiltrationCountryState.Error -> {
                            //
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
