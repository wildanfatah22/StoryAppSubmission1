package com.example.storyapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.DetailStory
import com.example.storyapp.databinding.ItemStoryBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class StoryAdapter(private val listStory: List<DetailStory>) :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DetailStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStory[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
        }
    }

    class ListViewHolder(private var binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DetailStory) {
            Glide.with(binding.root)
                .load(data.photoUrl)
                .into(binding.ivImage)
            binding.tvName.text = data.name
            binding.tvDescription.text = data.description
            binding.tvDate.text = data.createdAt
        }
    }

    override fun getItemCount(): Int = listStory.size

    companion object {
        @JvmStatic
        fun formatDateToString(dateString: String): String {
            val inputDateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val date: Date?
            var outputDate = ""

            try {
                date = inputDateFormat.parse(dateString)
                outputDate = outputDateFormat.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return outputDate
        }
    }
}