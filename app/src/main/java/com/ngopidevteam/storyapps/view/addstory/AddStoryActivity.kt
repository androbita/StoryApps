package com.ngopidevteam.storyapps.view.addstory

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ngopidevteam.storyapps.R
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import com.ngopidevteam.storyapps.databinding.ActivityAddStoryBinding
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var userPref: UserPreferences
    private var imageFile: File? = null
    private var currentImageUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenGallery.setOnClickListener { startGallery() }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {  uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Log.d("Photo Picker", "No Media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgAddPhoto.setImageURI(it)
        }
    }
}