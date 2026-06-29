package ru.practicum.android.diploma.ui.fragments.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.viewmodels.IndustryState
import ru.practicum.android.diploma.presentation.viewmodels.IndustryViewModel
import ru.practicum.android.diploma.ui.fragments.IndustryAdapter

class IndustrySelectionFragment : Fragment() {
    private var _binding: FragmentFilterIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IndustryViewModel by viewModel()
    private var adapter: IndustryAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        setupNoInternetState()
        setupNoFoundState()
        setupServerErrorState()

        viewModel.observeLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.selectedIndustryId.observe(viewLifecycleOwner) { id ->
            adapter?.setSelectedIndustry(id)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchIcon.setOnClickListener {
            binding.searchIndustry.setText("")
        }

        binding.choose.setOnClickListener {
            val selected = adapter?.getSelectedIndustry()
            if (selected != null) {
                returnIndustryResult(selected.industryName, selected.industryId)
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                binding.searchIcon.setImageResource(
                    if (query.isEmpty()) R.drawable.ic_search_24 else R.drawable.ic_close_24
                )
                viewModel.searchDebounce(query)
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
        binding.searchIndustry.addTextChangedListener(textWatcher)
    }

    private fun initRecyclerView() {
        adapter = IndustryAdapter { industry ->
            // При выборе отрасли показываем кнопку "Выбрать"
            binding.choose.isVisible = industry.industryName.isNotEmpty()
        }
        binding.industryList.adapter = adapter
        binding.industryList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: IndustryState) {
        when (state) {
            is IndustryState.IsLoading -> showLoading()
            is IndustryState.Error -> showError()
            is IndustryState.Empty -> showEmpty()
            is IndustryState.Content -> showContent(state.industry)
            is IndustryState.NoInternet -> showNoInternet()
        }
    }

    private fun showContent(found: List<Industry>) {
        binding.industryList.isVisible = true
        binding.layoutServerError.root.isVisible = false
        binding.layoutNoFound.root.isVisible = false
        binding.layoutNoInternet.root.isVisible = false
        binding.layoutLoading.root.isVisible = false
        adapter?.industrys = found
        adapter?.notifyDataSetChanged()

        // Восстанавливаем выбор из ViewModel после обновления списка
        adapter?.setSelectedIndustry(viewModel.selectedIndustryId.value)
    }

    private fun showError() {
        binding.industryList.isVisible = false
        binding.layoutServerError.root.isVisible = true
        binding.layoutNoFound.root.isVisible = false
        binding.layoutNoInternet.root.isVisible = false
        binding.layoutLoading.root.isVisible = false
    }

    private fun showEmpty() {
        binding.industryList.isVisible = false
        binding.layoutServerError.root.isVisible = false
        binding.layoutNoFound.root.isVisible = true
        binding.layoutNoInternet.root.isVisible = false
        binding.layoutLoading.root.isVisible = false
    }

    private fun showNoInternet() {
        binding.industryList.isVisible = false
        binding.layoutServerError.root.isVisible = false
        binding.layoutNoFound.root.isVisible = false
        binding.layoutNoInternet.root.isVisible = true
        binding.layoutLoading.root.isVisible = false
    }

    private fun showLoading() {
        binding.industryList.isVisible = false
        binding.layoutServerError.root.isVisible = false
        binding.layoutNoFound.root.isVisible = false
        binding.layoutNoInternet.root.isVisible = false
        binding.layoutLoading.root.isVisible = true
    }

    private fun returnIndustryResult(industryName: String?, industryId: String?) {
        val result = bundleOf(
            "industry_name" to industryName,
            "industry_id" to industryId
        )
        parentFragmentManager.setFragmentResult("industry_selection_result", result)
        findNavController().navigateUp()
    }

    private fun setupNoInternetState() {
        binding.layoutNoInternet.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_internet)
        binding.layoutNoInternet.tvPlaceholderText.text = getString(R.string.no_internet)
        binding.layoutNoInternet.tvPlaceholderText.isVisible = true
    }

    private fun setupNoFoundState() {
        binding.layoutNoFound.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_found)
        binding.layoutNoFound.tvPlaceholderText.text = getString(R.string.no_industry)
        binding.layoutNoFound.tvPlaceholderText.isVisible = true
    }

    private fun setupServerErrorState() {
        binding.layoutServerError.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_error_server)
        binding.layoutServerError.tvPlaceholderText.text = getString(R.string.server_error)
        binding.layoutServerError.tvPlaceholderText.isVisible = true
    }
}
