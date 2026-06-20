package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancySearchBinding

class VacancySearchFragment : Fragment() {
    private var _binding: FragmentVacancySearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVacancySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialState()
        setupNoInternetState()
        setupNoFoundState()

        binding.filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_vacancySearchFragment_to_filtersFragment)
        }

        binding.searchIcon.setOnClickListener {
            binding.searchEditText.setText("")
            showInitialState()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.searchIcon.setImageResource(R.drawable.ic_search_24)
                    showInitialState()
                } else {
                    binding.searchIcon.setImageResource(R.drawable.ic_close_24)
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }

        binding.searchEditText.addTextChangedListener(textWatcher)
    }

    private fun setupInitialState() {
        binding.layoutInitial.ivPlaceholderPicture.setImageResource(R.drawable.main_screen)
        binding.layoutInitial.tvPlaceholderText.isVisible = false
    }

    private fun setupNoInternetState() {
        binding.layoutNoInternet.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_internet)
        binding.layoutNoInternet.tvPlaceholderText.text = getString(R.string.no_internet)
        binding.layoutNoInternet.tvPlaceholderText.isVisible = true
    }

    private fun setupNoFoundState() {
        binding.layoutNoFound.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_found)
        binding.layoutNoFound.tvPlaceholderText.text = getString(R.string.error_fetching_vacancies)
        binding.layoutNoFound.tvPlaceholderText.isVisible = true
    }

    private fun showInitialState() {
        binding.layoutInitial.root.isVisible = true
        binding.layoutNoInternet.root.isVisible = false
        binding.layoutNoFound.root.isVisible = false
        binding.tvResultInfo.isVisible = false
    }

    private fun showNoInternetState() {
        binding.layoutInitial.root.isVisible = false
        binding.layoutNoInternet.root.isVisible = true
        binding.layoutNoFound.root.isVisible = false
        binding.tvResultInfo.isVisible = false
    }

    private fun showEmptyResultState() {
        binding.layoutInitial.root.isVisible = false
        binding.layoutNoInternet.root.isVisible = false
        binding.layoutNoFound.root.isVisible = true
        binding.tvResultInfo.isVisible = true
        binding.tvResultInfo.text = getString(R.string.no_vacancies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
