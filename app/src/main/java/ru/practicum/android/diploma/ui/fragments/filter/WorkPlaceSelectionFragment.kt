package ru.practicum.android.diploma.ui.fragments.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceSelectionBinding
import ru.practicum.android.diploma.ui.fragments.filter.FilterAreaFragment.Companion.AREA_DATA_KEY

class WorkPlaceSelectionFragment : Fragment() {
    private var _binding: FragmentWorkPlaceSelectionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWorkPlaceSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Установка кнопки "Назад"
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Установка внутренних переходов
        binding.goToCountrySelection.setOnClickListener {
            findNavController().navigate(R.id.action_workPlaceSelectionFragment_to_countrySelectionFragment)
        }

        binding.goToRegionSelection.setOnClickListener {
            findNavController().navigate(R.id.action_workPlaceSelectionFragment_to_regionSelectionFragment)
        }

        //получение id выранного региона
        val savedStateHandle =
            findNavController()
                .currentBackStackEntry
                ?.savedStateHandle

        savedStateHandle
            ?.getLiveData<Int>(AREA_DATA_KEY)
            ?.observe(viewLifecycleOwner) { areaId ->

                Log.d("getAreaId", "received = $areaId")

                savedStateHandle.remove<Int>(AREA_DATA_KEY)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
