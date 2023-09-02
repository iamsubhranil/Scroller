package com.thinkdifferent.scroller.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thinkdifferent.scroller.api.ImageRetrieveAPI
import com.thinkdifferent.scroller.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val api = ImageRetrieveAPI("SKSKSK")

        val imageDataList = InfiniteList(20) { page, pageSize ->
            api.retrieveImages(page, pageSize)
        }

        val imageView: RecyclerView = binding.imageList
        val imageViewAdapter = ImageViewAdapter(imageDataList)
        imageView.adapter = imageViewAdapter
        imageView.layoutManager = LinearLayoutManager(requireContext())

        imageDataList.loadMore()
        imageDataList.adapter = imageViewAdapter

        imageView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // Load more data when the user is near the end of the list
                if (lastVisibleItemPosition > totalItemCount * 0.75) {
                    imageDataList.loadMore()
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}