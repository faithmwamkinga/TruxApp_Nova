package com.project.trux_application.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.project.trux_application.databinding.ActivitySignupPageBinding
import com.project.trux_application.model.RegisterRequest
import com.project.trux_application.viewModel.UserViewModel



class SigupPageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupPageBinding
    val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.tvSign.setOnClickListener {
            val intent = Intent(this, LoginPageActivity::class.java) //Todo Login Activity
            startActivity(intent)

        }
        binding.btnSignUp.setOnClickListener {
            validateSignUP()

        }
        userViewModel.errLiveData.observe(this, Observer { err ->
            Toast.makeText(this, err, Toast.LENGTH_LONG).show()
//            binding.progressBar.visibility = View.GONE
        })

        userViewModel.regLiveData.observe(this, Observer { regResponse ->
//            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, regResponse.message, Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginPageActivity::class.java)) //Todo Login
        })


    }

    fun validateSignUP() {
        val userName = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val firstName = binding.etFisrName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val phone = binding.etPhone.text.toString()
        val licenseNumber = binding.etLicense.text.toString()
        val password = binding.etPassword.text.toString()


        var error = false
        if (firstName.isBlank()) {
            binding.tilFirstName.error = "First Name is required"
            error = true
        }
        if (lastName.isBlank()) {
            binding.tilLastName.error = "Last Name is required"
            error = true
        }
        if (userName.isBlank()) {
            binding.tilUserName.error = "Last Name is required"
            error = true
        }
        if (phone.isBlank()) {
            binding.tilPhone.error = "phone number is required"
            error = true
        }
        if (email.isBlank()) {
            binding.tilEmail.error = "Email is required"
            error = true
        }
        if (licenseNumber.isBlank()) {
            binding.tilLicense.error = "Email is required"
            error = true
        }

        if (password.isBlank()) {
            binding.tilPassword.error = "password is required"
            error = true
        }


        if (!error) {
            val registerRequest = RegisterRequest(
                username = userName,
                email = email,
                firstname = firstName,
                lastname = lastName,
                phone = phone,
                licensenumber = licenseNumber,
                password = password,
            )
            userViewModel.registerUser(registerRequest)
        }
    }
}
