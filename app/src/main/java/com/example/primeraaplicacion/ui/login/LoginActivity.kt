package com.example.primeraaplicacion.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.primeraaplicacion.R
import com.example.primeraaplicacion.core.base.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is LoginUiState.Idle -> {}
                is LoginUiState.Loading -> {}
                is LoginUiState.Success -> {
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                }
                is LoginUiState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            viewModel.login(
                username = etUsername.text.toString(),
                password = etPassword.text.toString()
            )
        }
    }
}
