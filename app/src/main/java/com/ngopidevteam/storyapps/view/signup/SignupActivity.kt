package com.ngopidevteam.storyapps.view.signup

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.databinding.ActivitySignupBinding
import com.ngopidevteam.storyapps.di.Injection
import com.ngopidevteam.storyapps.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val viewModel: SignupViewModel by viewModels{
        ViewModelFactory(Injection.provideRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(this) { result ->
            when(result){
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.signupButton.isEnabled = false
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.signupButton.isEnabled = true
                    Toast.makeText(this, "Registration Success: ${result.data.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.signupButton.isEnabled = true
                    Toast.makeText(this, "Registration Failed: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(name, email, password)

//            AlertDialog.Builder(this).apply {
//                setTitle("Yeah!")
//                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
//                setPositiveButton("Lanjut") { _, _ ->
//                    finish()
//                }
//                create()
//                show()
//            }
        }
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
    }
}