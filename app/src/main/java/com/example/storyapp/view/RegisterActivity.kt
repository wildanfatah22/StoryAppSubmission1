package com.example.storyapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.viewmodel.RegisterViewModel
import com.example.storyapp.customview.CustomEmailEditText
import com.example.storyapp.customview.CustomNameEditText
import com.example.storyapp.customview.CustomPasswordEditText
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.repository.MyRepository
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.viewmodel.LoginViewModel
import com.example.storyapp.viewmodel.RegisterViewModelFactory
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myRepository = MyRepository(ApiConfig.getApiService())
        val viewModel = ViewModelProvider(this, RegisterViewModelFactory(myRepository)).get(RegisterViewModel::class.java)

        ifClicked()

        binding.goLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        registerViewModel.messageRegist.observe(this) { message ->
            responseRegister(
                registerViewModel.isErrorRegist,
                message
            )
        }

        registerViewModel.isLoadingRegist.observe(this) {
            showLoading(it)
        }

    }

//    private fun responseRegister(
//        isError: Boolean,
//        message: String,
//    ) {
//        if (!isError) {
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//            val userLogin = LoginAccount(
//                binding.edtInputemail.text.toString(),
//                binding.edtInputpassword.text.toString()
//            )
//            mainViewModel.getResponseLogin(userLogin)
//        } else {
//            if (message == "1") {
//                emailEditText.setErrorMessage(resources.getString(R.string.emailTaken), emailEditText.text.toString())
//                Toast.makeText(this, resources.getString(R.string.emailTaken), Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun responseRegister(
        isError: Boolean,
        message: String,
    ) {
        val emailEditText: CustomEmailEditText = binding.edtInputemail
        val passwordEditText: CustomPasswordEditText = binding.edtInputpassword
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val userLogin = LoginAccount(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
            loginViewModel.getResponseLogin(userLogin)
        } else {
            if (message == "1") {
                Toast.makeText(this, resources.getString(R.string.email_taken), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ifClicked() {
        val registerButton: MaterialButton = binding.btnRegister
        val nameEditText: CustomNameEditText = binding.edtInputname
        val emailEditText: CustomEmailEditText = binding.edtInputemail
        val passwordEditText: CustomPasswordEditText = binding.edtInputpassword

        registerButton.setOnClickListener {
            binding.apply {
                nameEditText.clearFocus()
                emailEditText.clearFocus()
                passwordEditText.clearFocus()
            }

            if (nameEditText.isNameValid && emailEditText.isEmailValid && passwordEditText.isPasswordValid) {
                val dataRegisterAccount = RegisterAccount(
                    name = nameEditText.text.toString().trim(),
                    email = emailEditText.text.toString().trim(),
                    password = passwordEditText.text.toString().trim()
                )

                registerViewModel.getResponseRegister(dataRegisterAccount)
            } else {
                if (!nameEditText.isNameValid) nameEditText.error =
                    resources.getString(R.string.name_none)
                if (!emailEditText.isEmailValid) emailEditText.error =
                    resources.getString(R.string.email_none)
                if (!passwordEditText.isPasswordValid) passwordEditText.error =
                    resources.getString(R.string.password_none)

                Toast.makeText(this, R.string.login_invalid, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
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
    **/

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



}