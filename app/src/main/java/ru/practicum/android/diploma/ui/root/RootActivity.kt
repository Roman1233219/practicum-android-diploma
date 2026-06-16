package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.presentation.SomeViewModel

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    private val someViewModel: SomeViewModel by viewModel()
    private val networkClient: NetworkClient by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Для проверки DI
        someViewModel.data.observe(
            this,
            Observer { newData ->
                Toast.makeText(this, "$newData", Toast.LENGTH_SHORT).show()
            }
        )

        // Подключение NavController к BottomBar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Тестовый вызов API HH из Activity
        lifecycleScope.launch {
            val testData = networkClient.searchVacancies(VacanciesRequest())
            Log.d("vacancies", "HTTP result: ${testData.resultCode}")
            if(testData is VacanciesResponse) {
                Log.d("vacancies", "Page: ${testData.page}")
                Log.d("vacancies", "Pages: ${testData.pages}")
                Log.d("vacancies", "Found: ${testData.found}")
                Log.d("vacancies", "First vacancy name: ${testData.items.firstOrNull()?.name}")
            }
        }
    }

    companion object {
        private const val BOTTOM_NAV_VERTICAL_PADDING_DP = 8f
    }
}
