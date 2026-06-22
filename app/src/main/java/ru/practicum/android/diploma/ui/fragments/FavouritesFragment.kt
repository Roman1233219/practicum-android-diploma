package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavouritesBinding
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.ui.adapter.FavoritesAdapter
import ru.practicum.android.diploma.ui.viewmodels.FavoritesState
import ru.practicum.android.diploma.ui.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.util.debounce
import kotlin.getValue

class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private lateinit var onVacancyCardDebounce: (VacancyCard) -> Unit
    private var adapter: FavoritesAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoritesAdapter { vacancy ->
            onVacancyCardDebounce(vacancy)
        }

        onVacancyCardDebounce =
            debounce<VacancyCard>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { vacancy ->
                findNavController().navigate(R.id.action_favouritesFragment_to_vacancyDetailsFragment)
            }
    }

    fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> showContent(state.vacancy)
            is FavoritesState.IsEmpty -> showEmpty()
            is FavoritesState.ConnectionError -> showConnectionError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
        binding.vacancy_list.adapter = null
    }

    private fun showEmpty() {
        binding.vacancy_list.visibility = View.GONE
        binding.error_placeholder.visibility = View.GONE
        binding.no_items_placeholder.visibility = View.VISIBLE
    }

    private fun showConnectionError() {
        binding.error_placeholder.visibility = View.VISIBLE
        binding.vacancy_list.visibility = View.GONE
        binding.no_items_placeholder.visibility = View.GONE
    }

    private fun showContent(foundTrack: List<VacancyCard>) {
        binding.vacancy_list.visibility = View.VISIBLE
        binding.no_items_placeholder.visibility = View.GONE
        binding.error_placeholder.visibility = View.GONE
        adapter?.vacancy = foundTrack
        adapter?.notifyDataSetChanged()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavouritesFragment()
    }

}
