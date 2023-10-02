package com.example.storyapp.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.R
import com.example.storyapp.viewmodel.LoginViewModel
import com.example.storyapp.customview.CustomEmailEditText
import com.example.storyapp.customview.CustomPasswordEditText

import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.databinding.ActivityLoginBinding
import com.google.android.material.button.MaterialButton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authChecked()

        binding.goRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun authChecked() {
        val loginButton: MaterialButton = binding.btnLogin
        val emailEditText: CustomEmailEditText = binding.edtEmailInput
        val passwordEditText: CustomPasswordEditText = binding.edtPasswordInput

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val loginAccount = LoginAccount(email, password)

            // Lakukan proses autentikasi sesuai kebutuhan Anda
            if (email.isEmpty() && password.isEmpty()) {
                emailEditText.error = getString(R.string.email_none)
                emailEditText.requestFocus()
                passwordEditText.error = getString(R.string.password_none)
                passwordEditText.requestFocus()
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
