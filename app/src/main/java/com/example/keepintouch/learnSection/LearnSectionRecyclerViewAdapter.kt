package com.example.keepintouch.learnSection

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.keepintouch.R

class LearnSectionRecyclerViewAdapter(private val values: List<DataItem>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

    class CategoryHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.video_category_name)
        fun bind(item: DataItem.CategoryName) {
            title.text = item.name
        }
    }

    class LearnVideosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.video_title)
        val description: TextView = view.findViewById(R.id.video_description)
        var url: String? = null
        fun bind(item: DataItem.VideoItem) {
            title.text = item.video.name
            description.text = item.video.description
            url = item.video.url
            itemView.setOnClickListener { view: View? ->
                view?.findNavController()?.navigate(
                    R.id.action_learnSectionFragment_to_videoFragment,
                    bundleOf("url" to item.video.url)
                )
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (values[position]) {
            is DataItem.CategoryName -> ITEM_VIEW_TYPE_HEADER
            is DataItem.VideoItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ITEM_VIEW_TYPE_HEADER -> CategoryHeaderViewHolder(parent.inflate(R.layout.learn_category_header))
        ITEM_VIEW_TYPE_ITEM -> LearnVideosViewHolder(parent.inflate(R.layout.fragment_item))
        else -> LearnVideosViewHolder(parent.inflate(R.layout.fragment_item))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = values[holder.adapterPosition]) {
            is DataItem.VideoItem -> (holder as LearnVideosViewHolder).bind(item)
            is DataItem.CategoryName -> (holder as CategoryHeaderViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = values.size
}

