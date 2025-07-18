package com.ngopidevteam.storyapps.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.databinding.ActivityLoginBinding
import com.ngopidevteam.storyapps.view.ViewModelFactory
import com.ngopidevteam.storyapps.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeUserLoginState()
        setupView()
        setupAction()
        observeLoginResult()
    }

    private fun observeUserLoginState() {
        viewModel.user.observe(this) { user ->
            if (user.isLogin) {
                moveToMainActivity()
            }
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    moveToMainActivity()
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    Toast.makeText(this, "Login failed: ${result.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("LoginActivity", "Login gagal: ${result.message}")
                }
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun setupAction() {

        binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Semua field harus diisi")
                return@setOnClickListener
            }

            viewModel.loginUser(email, password)

            viewModel.user.observe(this) { user ->
                if (user.isLogin) {
                    Toast.makeText(this, "Success login as : ${user.email}", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("LoginActivity", "Berhasil Login sebagai : ${user.token}")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}