package ru.practicum.android.diploma.ui.root

import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.NetworkClient

import ru.practicum.android.diploma.databinding.ActivityRootBinding


class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    private val networkClient: NetworkClient by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Подключение NavController к BottomBar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.vacancySearchFragment,
                R.id.favouritesFragment,
                R.id.teamFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.divider.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.divider.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        private const val BOTTOM_NAV_VERTICAL_PADDING_DP = 8f
    }
}
