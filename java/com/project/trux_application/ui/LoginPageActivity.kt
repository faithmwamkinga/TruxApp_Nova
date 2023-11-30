package com.project.trux_application.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.project.trux_application.databinding.ActivityLoginPageBinding
import com.project.trux_application.model.LoginRequest
import com.project.trux_application.viewModel.LoginViewModel



class LoginPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding
    val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        binding.btnLogin.setOnClickListener {
            validateLogin()
        }
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SigupPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginViewModel.errLiveData.observe(this, Observer { err->
            Toast.makeText(this, err, Toast.LENGTH_LONG).show()
        })

        loginViewModel.loginLiveData.observe(this, Observer { loginResponse->
            Toast.makeText(this, loginResponse.message, Toast.LENGTH_SHORT).show()

//             Store the authentication token in SharedPreferences
            val authToken = loginResponse.token
            if (authToken != null) {
                val formattedToken = "Token $authToken"
                val editor = sharedPreferences.edit()
                editor.putString("auth_token", formattedToken)
                editor.apply()
            }

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        })


    }

    fun validateLogin() {
        var username = binding.etUsername.text.toString()
        var password = binding.etPassword.text.toString()
        var error = false
        if (username.isBlank()) {
            binding.tilUsername.error = "Email required"
            error = true
        }
        if (password.isBlank()) {
            binding.tilPassword.error = "Password required"
            error = true
        }
        if (!error) {
            val loginRequest = LoginRequest(
                username = username,
                password = password
            )
            // Call loginUser() and handle navigation in the observer for loginLiveData
            loginViewModel.loginUser(loginRequest)
        }
    }


}










