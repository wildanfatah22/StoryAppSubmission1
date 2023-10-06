package com.example.storyapp.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.adapter.StoryAdapter
import com.example.storyapp.data.datastore.UserPreferences
import com.example.storyapp.data.response.DetailStory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.viewmodel.MainViewModel
import com.example.storyapp.viewmodel.UserAuthViewModel
import com.example.storyapp.viewmodel.UserAuthViewModelFactory

class MainActivity : AppCompatActivity() {
    private val pref = UserPreferences.getInstance(dataStore)
    private lateinit var binding: ActivityMainBinding
    private lateinit var token: String
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
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
            .setNegativeButton(getString(R.string.logout)) { _, _ ->
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

    private fun setUserData(storyList: List<DetailStory>) {
        showNoData(storyList.isEmpty())

        val adapter = StoryAdapter(storyList)
        binding.rvStories.adapter = adapter

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DetailStory) {
                navigateToUserDetail(data)
            }
        })
    }

    private fun navigateToUserDetail(data: DetailStory) {
        val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.KEY_DATA,data)
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle())
    }

    private fun observeData() {
        val userAuthViewModel =
            ViewModelProvider(this, UserAuthViewModelFactory(pref))[UserAuthViewModel::class.java]

        userAuthViewModel.getToken().observe(this) {
            token = it
            mainViewModel.getStories(token)
        }

        mainViewModel.message.observe(this) {
            setUserData(mainViewModel.stories)
            showToast(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
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
            R.id.logout -> {
                showAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(msg: String) {
        if (mainViewModel.isError) {
            Toast.makeText(
                this,
                "${getString(R.string.error_load)} $msg",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showNoData(isNoData: Boolean) {
        binding.noDataFound.visibility = if (isNoData) View.VISIBLE else View.GONE
    }


}