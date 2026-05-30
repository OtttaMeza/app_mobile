package com.example.primeraaplicacion.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.primeraaplicacion.R
import com.example.primeraaplicacion.core.base.BaseActivity
import com.example.primeraaplicacion.ui.second.SecondActivity

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MainViewModelFactory())[MainViewModel::class.java]

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainUiState.Loading -> {}
                is MainUiState.Success -> {
                    Toast.makeText(this, "${state.screens.size} pantallas cargadas", Toast.LENGTH_SHORT).show()
                }
                is MainUiState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.loadScreens()

        findViewById<Button>(R.id.btnNavigate).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}
