package com.example.keepintouch.learnSection

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.example.keepintouch.R
import com.example.keepintouch.service.learnApiService
import kotlinx.android.synthetic.main.fragment_item_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

sealed class DataItem {
    data class VideoItem(val video: Video): DataItem()
    data class CategoryName(val name: String): DataItem()
}

class LearnSectionFragment : Fragment() {

    private val _response = MutableLiveData<List<DataItem>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        _response.value = listOf()
        generateMockVideos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_item_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        learn_video_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = LearnSectionRecyclerViewAdapter(_response.value!!)
        }
    }

    private fun generateMockVideos() {
        learnApiService.getLessons().enqueue(object: Callback<List<Video>> {
            override fun onFailure(call: Call<List<Video>>, t: Throwable) {
                println("Failure: " + t.message)
            }

            override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                println(response)
                _response.value = response.body()
                    ?.groupBy { it.category }
                    ?.flatMap { (category, videos) -> listOf<DataItem>(DataItem.CategoryName(category.name)) + videos.map { DataItem.VideoItem(it) } }
                learn_video_list.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = LearnSectionRecyclerViewAdapter(_response.value!!)
                }
            }
        })
    }

}