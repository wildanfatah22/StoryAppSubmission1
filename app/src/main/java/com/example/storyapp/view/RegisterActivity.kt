package com.example.storyapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.viewmodel.RegisterViewModel
import com.example.storyapp.customview.CustomEmailEditText
import com.example.storyapp.customview.CustomNameEditText
import com.example.storyapp.customview.CustomPasswordEditText
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        authChecked()


        binding.goLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun authChecked() {
        val registerButton: MaterialButton = binding.btnRegister
        val nameEditText: CustomNameEditText = binding.edtInputname
        val emailEditText: CustomEmailEditText = binding.edtInputemail
        val passwordEditText: CustomPasswordEditText = binding.edtInputpassword

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val loginAccount = LoginAccount(email, password)

            // Lakukan proses autentikasi sesuai kebutuhan Anda
            if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                nameEditText.error = getString(R.string.name_none)
                nameEditText.requestFocus()
                emailEditText.error = getString(R.string.email_none)
                emailEditText.requestFocus()
                passwordEditText.error = getString(R.string.password_none)
                passwordEditText.requestFocus()
            } else if (name.isEmpty()){
                nameEditText.error = getString(R.string.name_none)
                nameEditText.requestFocus()
            } else if (email.isEmpty()) {
                emailEditText.error = getString(R.string.email_none)
                emailEditText.requestFocus()
            } else if (password.isEmpty()) {
                passwordEditText.error = getString(R.string.password_none)
                passwordEditText.requestFocus()
            } else {
//                viewModel.loginUser(loginAccount)
            }
        }
    }



}