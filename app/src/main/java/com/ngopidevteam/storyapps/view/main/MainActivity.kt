package com.ngopidevteam.storyapps.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.data.adapter.StoryAdapter
import com.ngopidevteam.storyapps.databinding.ActivityMainBinding
import com.ngopidevteam.storyapps.remote.response.ListStoryItem
import com.ngopidevteam.storyapps.view.detail.DetailActivity
import com.ngopidevteam.storyapps.view.ViewModelFactory
import com.ngopidevteam.storyapps.view.addstory.AddStoryActivity
import com.ngopidevteam.storyapps.view.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private var storyAdapter = StoryAdapter(ArrayList<ListStoryItem>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        observeUser()
        observeStories()
        setupRecView()
        setupAction()
        addStory()
    }

    private fun addStory() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeStories() {
        viewModel.storiesResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    val stories = result.data.listStory
                    if (stories.isNotEmpty()){
                        storyAdapter.submitList(stories)
                        binding.tvEmpty.visibility = View.GONE
                    }else{
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                }
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecView() {
//        storyAdapter = StoryAdapter{ story, imageView ->
//            val intent = Intent(this, DetailActivity::class.java).apply {
//                putExtra(DetailActivity.EXTRA_STORY, story)
//                putExtra(DetailActivity.EXTRA_TRANSITION_NAME, imageView.transitionName)
//            }
//
//            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this,
//                imageView,
//                imageView.transitionName
//            )

//            startActivity(intent, optionsCompat.toBundle())

//        }

        binding.recViewStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun observeUser() {
//        val userRepository = Injection.provideRepository(this)

//        using livedata
        viewModel.getSession().observe(this) { result ->
            if (result.isLogin){
                viewModel.getStories(
                    page = 1,
                    size = 10,
                    location = 0
                )
            }
        }

//        using coroutine
//        lifecycleScope.launch {
//            userRepository.getSession().collect { result ->
//                if (result.isLogin){
//                    viewModel.getStories(
//                        token = result.token,
//                        page = 1,
//                        size = 10,
//                        location = 0
//                    )
//                }
//            }
//        }
    }

    private fun setupAction() {
//        val userRepository = Injection.provideRepository(this)
//        val userLiveData: LiveData<UserModel> = userRepository.getSession().asLiveData()

//        lifecycleScope.launch {
//            userRepository
//        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()
            moveToLogin()
        }
//        binding.logoutButton.setOnClickListener {
//            lifecycleScope.launch {
//                userRepository.logout()
//            }
//
//            viewModel.logout()
//
//            startActivity(Intent(this, WelcomeActivity::class.java))
//
//        }
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}