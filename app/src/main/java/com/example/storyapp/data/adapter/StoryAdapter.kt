package com.example.storyapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.databinding.ItemStoryBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class StoryAdapter:
    PagingDataAdapter<StoryDetailResponse, StoryAdapter.ListViewHolder>(StoryDetailDiffCallback()) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryDetailResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            getItem(position)?.let { it1 -> onItemClickCallback.onItemClicked(it1) }
        }
    }

    class ListViewHolder(private var binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryDetailResponse) {
            binding.tvName.text = data.name
            val maxDescriptionLength = 150

            val descriptionText = if (data.description?.length!! > maxDescriptionLength) {
                data.description.substring(0, maxDescriptionLength) + "..."
            } else {
                data.description
            }

            binding.tvDescription.text = descriptionText


            binding.tvDate.text = formatDateToString(data.createdAt.toString())
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(binding.ivImage)
        }
    }


    class StoryDetailDiffCallback : DiffUtil.ItemCallback<StoryDetailResponse>() {
        override fun areItemsTheSame(oldItem: StoryDetailResponse, newItem: StoryDetailResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StoryDetailResponse,
            newItem: StoryDetailResponse
        ): Boolean {
            return oldItem == newItem
        }
    }


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