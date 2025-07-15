package com.ngopidevteam.storyapps.data.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.core.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ngopidevteam.storyapps.R
import com.ngopidevteam.storyapps.remote.response.ListStoryItem
import com.ngopidevteam.storyapps.view.detail.DetailActivity

class StoryAdapter(private val listStory: ArrayList<ListStoryItem>): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    fun submitList(stories: List<ListStoryItem>){
        listStory.clear()
        listStory.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story, parent, false)

        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StoryViewHolder,
        position: Int
    ) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    class StoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val tvJudul: TextView = itemView.findViewById(R.id.tv_judul)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)

        fun bind(story: ListStoryItem){
            tvJudul.text = story.name
            tvDescription.text = story.description

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.image_dicoding)
                .into(imgPhoto)


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"),
                        Pair(tvJudul, "judul"),
                        Pair(tvDescription, "description")
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}