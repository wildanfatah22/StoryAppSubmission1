package com.example.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.datastore.UserPreferences
import com.example.storyapp.databinding.ActivitySplashBinding
import com.example.storyapp.viewmodel.UserAuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModelFactory

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore)
        val loginViewModel =
            ViewModelProvider(this, UserAuthViewModelFactory(pref))[UserAuthViewModel::class.java]

        loginViewModel.getLoginSession().observe(this) { isLoggedIn ->
            playAnimation()

            val intent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }


            val SPLASH_DELAY: Long = 3000

            binding.tvLogo.animate()
                .setDuration(SPLASH_DELAY)
                .alpha(0f)
                .withEndAction {
                    startActivity(intent)
                    finish()
                }


        }

    }

    private fun playAnimation() {
        val splashScreenTextLogo =
            ObjectAnimator.ofFloat(binding.tvSplashScreen, View.ALPHA, 1f).setDuration(2000)
        val splashScreenTextBottom =
            ObjectAnimator.ofFloat(binding.bottomSplashScreen, View.ALPHA, 1f).setDuration(2000)

        AnimatorSet().apply {
            playTogether(splashScreenTextBottom, splashScreenTextLogo)
            start()
        }

        binding.tvLogo.animate()
            .setDuration(3000)
            .alpha(0f)
            .withEndAction {
                startActivity(intent)
                finish()
            }
    }
}