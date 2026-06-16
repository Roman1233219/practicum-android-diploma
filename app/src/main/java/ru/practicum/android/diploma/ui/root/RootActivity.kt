package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.SomeViewModel

class RootActivity : AppCompatActivity() {
    private val someViewModel: SomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        //Для проверки DI
        val textView = findViewById<TextView>(R.id.buildConfigReadExampleTextView)
        someViewModel.data.observe(this, Observer { newData ->
            textView.text = newData
        })
    }
}
