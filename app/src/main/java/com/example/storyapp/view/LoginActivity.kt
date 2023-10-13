package com.example.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.customview.CustomEmailEditText
import com.example.storyapp.customview.CustomPasswordEditText
import com.example.storyapp.data.local.datastore.UserPreferences
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.viewmodel.AuthViewModel
import com.example.storyapp.viewmodel.AuthViewModelFactory
import com.example.storyapp.viewmodel.UserAuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModelFactory
import com.google.android.material.button.MaterialButton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory(this))[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonClicked()
        playAnimation()

        val preferences = UserPreferences.getInstance(dataStore)
        val userAuthViewModel =
            ViewModelProvider(this, UserAuthViewModelFactory(preferences))[UserAuthViewModel::class.java]

        userAuthViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        authViewModel.message.observe(this) { message ->
            loginResponse(
                message,
                userAuthViewModel
            )
        }

        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.goRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loginResponse(message: String, userAuthViewModel: UserAuthViewModel) {
        if (message.contains("Hello")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = authViewModel.userLogin.value
            userAuthViewModel.saveLoginSession(true)
            userAuthViewModel.saveToken(user?.loginResult!!.token)
            userAuthViewModel.saveName(user.loginResult.name)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun buttonClicked() {
        val loginButton: MaterialButton = binding.btnLogin
        val emailEditText: CustomEmailEditText = binding.edtEmailInput
        val passwordEditText: CustomPasswordEditText = binding.edtPasswordInput

       loginButton.setOnClickListener {
            emailEditText.clearFocus()
            passwordEditText.clearFocus()

            if (emailEditText.isEmailValid && passwordEditText.isPasswordValid) {
                val requestLogin = LoginAccount(
                    emailEditText.text.toString().trim(),
                    passwordEditText.text.toString().trim()
                )
                authViewModel.login(requestLogin)
            } else {
                if (!emailEditText.isEmailValid) emailEditText.error =
                    getString(R.string.email_none)
                if (!passwordEditText.isPasswordValid) passwordEditText.error =
                    getString(R.string.password_none)

                Toast.makeText(this, R.string.login_invalid, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.tvLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(300)
        val edtPassword =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(300)
        val haveAcc =
            ObjectAnimator.ofFloat(binding.haveAcc, View.ALPHA, 1f).setDuration(300)
        val tvRegister =
            ObjectAnimator.ofFloat(binding.goRegister, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                tvLogin,
                tvLogin,
                edtEmail,
                edtPassword,
                btnLogin,
                haveAcc,
                tvRegister
            )
            start()
        }
    }
}