package com.example.storyapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.adapter.LoadingStateAdapter
import com.example.storyapp.data.adapter.StoryAdapter
import com.example.storyapp.data.local.datastore.UserPreferences
import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.viewmodel.MainViewModel
import com.example.storyapp.viewmodel.MainViewModelFactory
import com.example.storyapp.viewmodel.UserAuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModelFactory

class MainActivity : AppCompatActivity() {
    private val pref = UserPreferences.getInstance(dataStore)
    private lateinit var binding: ActivityMainBinding
    private lateinit var token: String

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.app_name)
        setUpUI()

    }

    private fun setUpUI() {
        buttonClicked()
        setUpRecyclerView()
        observeData()
    }

    private fun buttonClicked() {
        binding.btnFab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.getStories(token)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun logout() {
        val loginViewModel =
            ViewModelProvider(this, UserAuthViewModelFactory(pref))[UserAuthViewModel::class.java]
        loginViewModel.clearDataLogin()
        Toast.makeText(this, R.string.logout_success, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        val alert = builder.create()
        builder
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_description))
            .setPositiveButton(getString(R.string.logout_cancel)) { _, _ ->
                alert.cancel()
            }
            .setNegativeButton(getString(R.string.yes)) { _, _ ->
                logout()
            }
            .show()
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)
    }

    private fun observeData() {
        val userAuthViewModel =
            ViewModelProvider(this, UserAuthViewModelFactory(pref))[UserAuthViewModel::class.java]

        userAuthViewModel.getToken().observe(this) {
            token = it
            setUserData(it)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun setUserData(token: String) {

        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            })

        mainViewModel.getPagingStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryDetailResponse) {
                navigateToUserDetail(data)
            }
        })
    }

    private fun navigateToUserDetail(data: StoryDetailResponse) {
        val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.KEY_DATA, data)
        startActivity(
            intent,
            ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            R.id.see_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }

            R.id.logout -> {
                showAlertDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}