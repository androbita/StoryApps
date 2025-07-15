package com.ngopidevteam.storyapps.view.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ngopidevteam.storyapps.databinding.ActivityDetailBinding
import com.ngopidevteam.storyapps.remote.response.ListStoryItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object{
        const val EXTRA_STORY = "extra_story"
        const val EXTRA_TRANSITION_NAME = "extra_transition"
    }

//    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        val transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME)

        binding.imgDetailPhoto.transitionName = transitionName

        story?.let { story ->
            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.imgDetailPhoto)

            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
        }
    }
}