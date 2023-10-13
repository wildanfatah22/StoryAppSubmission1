package com.example.storyapp.view


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.customview.CustomEmailEditText
import com.example.storyapp.customview.CustomNameEditText
import com.example.storyapp.customview.CustomPasswordEditText
import com.example.storyapp.data.local.datastore.UserPreferences
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.viewmodel.AuthViewModel
import com.example.storyapp.viewmodel.AuthViewModelFactory
import com.example.storyapp.viewmodel.UserAuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModelFactory
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory(this))[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        buttonClicked()

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

        authViewModel.message.observe(this) { messageRegister ->
            registerResponse(
                messageRegister
            )
        }

        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        authViewModel.message.observe(this) { messageLogin ->
            loginResponse(
                messageLogin,
                userAuthViewModel
            )
        }

        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    private fun registerResponse(message: String) {
        if (message == "Yeay akun berhasil dibuat") {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val userLogin = LoginAccount(
                binding.edtInputemail.text.toString(),
                binding.edtInputpassword.text.toString()
            )
            authViewModel.login(userLogin)
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

    private fun buttonClicked() {
        val registerButton: MaterialButton = binding.btnRegister
        val nameEditText: CustomNameEditText = binding.edtInputname
        val emailEditText: CustomEmailEditText = binding.edtInputemail
        val passwordEditText: CustomPasswordEditText = binding.edtInputpassword
        val btnBack: ImageButton = binding.btnBack

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

                authViewModel.register(dataRegisterAccount)
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

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.tvRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTv = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(100)
        val nameTv = ObjectAnimator.ofFloat(binding.edtInputname, View.ALPHA, 1f).setDuration(100)
        val editNameTv = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.edtInputemail, View.ALPHA, 1f).setDuration(100)
        val editEmailTv = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(100)
        val passwordTv = ObjectAnimator.ofFloat(binding.edtInputpassword, View.ALPHA, 1f).setDuration(100)
        val editPasswordTv = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val btnSignup = ObjectAnimator.ofFloat(binding.haveAcc, View.ALPHA, 1f).setDuration(100)
        val btnSignup2 = ObjectAnimator.ofFloat(binding.goLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(titleTv, nameTv, editNameTv, emailTv, editEmailTv, passwordTv, editPasswordTv, btnSignup, btnSignup2)
            startDelay = 100
        }.start()
    }

}