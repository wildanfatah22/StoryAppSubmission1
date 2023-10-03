package com.example.storyapp.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.customview.CustomEmailEditText
import com.example.storyapp.customview.CustomNameEditText
import com.example.storyapp.customview.CustomPasswordEditText
import com.example.storyapp.data.datastore.UserPreferences
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount

import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.viewmodel.AuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModelFactory
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authChecked()

        val preferences = UserPreferences.getInstance(dataStore)
        val userAuthViewModel =
            ViewModelProvider(this, UserAuthViewModelFactory(preferences))[UserAuthViewModel::class.java]
        userAuthViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        authViewModel.messageRegister.observe(this) { messageRegister ->
            registerResponse(
                authViewModel.isErrorRegister,
                messageRegister
            )
        }

        authViewModel.isLoadingRegister.observe(this) {
            showLoading(it)
        }

        authViewModel.messageLogin.observe(this) { messageLogin ->
            loginResponse(
                authViewModel.isErrorLogin,
                messageLogin,
                userAuthViewModel
            )
        }

        authViewModel.isLoadingLogin.observe(this) {
            showLoading(it)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun loginResponse(isError: Boolean, message: String, userLoginViewModel: UserAuthViewModel) {
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = authViewModel.userLogin.value
            userLoginViewModel.saveLoginSession(true)
            userLoginViewModel.saveToken(user?.loginResult!!.token)
            userLoginViewModel.saveName(user.loginResult.name)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerResponse(isError: Boolean, message: String, ) {
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val userLogin = LoginAccount(
                binding.edtInputemail.text.toString(),
                binding.edtInputpassword.text.toString()
            )
            authViewModel.getResponseLogin(userLogin)
        } else {
            if (message == "1") {
                binding.edtInputemail.setMessage(resources.getString(R.string.email_taken), binding.edtInputemail.text.toString())
                Toast.makeText(this, resources.getString(R.string.email_taken), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authChecked() {
        val registerButton: MaterialButton = binding.btnRegister
        val nameEditText: CustomNameEditText = binding.edtInputname
        val emailEditText: CustomEmailEditText = binding.edtInputemail
        val passwordEditText: CustomPasswordEditText = binding.edtInputpassword

        registerButton.setOnClickListener {
            nameEditText.clearFocus()
            emailEditText.clearFocus()
            passwordEditText.clearFocus()

            if (nameEditText.isNameValid && emailEditText.isEmailValid && passwordEditText.isPasswordValid) {
                val dataRegisterAccount = RegisterAccount(
                    name = nameEditText.text.toString().trim(),
                    email = emailEditText.text.toString().trim(),
                    password = passwordEditText.text.toString().trim()
                )

                authViewModel.getResponseRegister(dataRegisterAccount)
            } else {
                if (!nameEditText.isNameValid) nameEditText.error =
                    resources.getString(R.string.name_none)
                if (!emailEditText.isEmailValid) emailEditText.error =
                    resources.getString(R.string.email_taken)
                if (!passwordEditText.isPasswordValid) passwordEditText.error =
                    resources.getString(R.string.password_none)
                Toast.makeText(this, R.string.login_invalid, Toast.LENGTH_SHORT).show()
            }
        }
    }

}