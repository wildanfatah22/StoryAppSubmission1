package com.example.storyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.adapter.StoryAdapter
import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.data.response.DetailStory
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.helper.LocationConverter

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val story = intent.getParcelableExtra<StoryDetailResponse>(KEY_DATA) as StoryDetailResponse
        setStory(story)
        supportActionBar?.title = getString(R.string.detail_title, story.name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setStory(story: StoryDetailResponse) {
        binding.apply {
            tvName.text = story.name
            tvDescription.text = story.description
            tvDate.text = StoryAdapter.formatDateToString(story.createdAt.toString())
        }

        binding.tvLocation.text = LocationConverter.getStringAddress(
            LocationConverter.toLatlng(story.lat, story.lon),
            this
        )
        Glide.with(this)
            .load(story.photoUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.ivStory)
    }

    companion object {
        const val KEY_DATA = "Data"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}