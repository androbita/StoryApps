package com.ngopidevteam.storyapps.view.addstory

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ngopidevteam.storyapps.data.ResultState
import com.ngopidevteam.storyapps.data.model.UserModel
import com.ngopidevteam.storyapps.data.pref.UserPreferences
import com.ngopidevteam.storyapps.databinding.ActivityAddStoryBinding
import com.ngopidevteam.storyapps.di.Injection
import com.ngopidevteam.storyapps.helper.getImageUri
import com.ngopidevteam.storyapps.helper.reduceImageFile
import com.ngopidevteam.storyapps.helper.uriToFile
import com.ngopidevteam.storyapps.view.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

        userPref = UserPreferences.getInstance(applicationContext)
        val factory = ViewModelFactory(Injection.provideRepository(this))
        viewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]

        binding.btnOpenGallery.setOnClickListener { startGallery() }
        binding.btnOpenCamera.setOnClickListener { startCamera() }
        binding.btnUploadStory.setOnClickListener { uploadStory() }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uploadState.observe(this) { state ->
            when (state) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnUploadStory.isEnabled = false
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnUploadStory.isEnabled = true
                    Toast.makeText(this, "Story berhasil diupload", Toast.LENGTH_SHORT).show()
                    finish()
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnUploadStory.isEnabled = true
                    Toast.makeText(
                        this,
                        "Story gagal diupload ${state.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess && currentImageUri != null) {
            imageFile = uriToFile(currentImageUri!!, this)
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            imageFile = uriToFile(uri, this)
            showImage()
        } else {
            Log.d("Photo Picker", "No Media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgAddPhoto.setImageURI(it)
        }
    }

    private fun uploadStory() {
        val description = binding.edtAddDescription.text.toString()

        if (description.isEmpty()) {
            Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageFile == null) {
            Toast.makeText(this, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val file = reduceImageFile(imageFile!!)

        if (file.length() > 1000000) {
            Toast.makeText(
                this,
                "Ukuran file melebihi 1MB, gunakan gambar lain",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val descRequestBody = description.toRequestBody("text/plain".toMediaType())
        val imageRequestBody = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            imageRequestBody
        )

        lifecycleScope.launch {
            val user: UserModel = userPref.getSession().first()
            viewModel.uploadStory(descRequestBody, imageMultipart)

            Log.d("test", "${user.token}, $descRequestBody, $imageMultipart")
        }

        Log.d("UploadDebug", "File exists: ${imageFile?.exists()} size: ${imageFile?.length()}")
    }
}