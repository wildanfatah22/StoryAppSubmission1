package com.example.storyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.R

class DetailStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)
    }

    companion object {
        const val KEY_DATA = "Data"
        private const val TAG = "DetailStoryActivity"
        const val KEY_USERNAME = "username"
        const val KEY_ID = "extra id"
        const val KEY_PHOTO = "extra_photo"

    }
}