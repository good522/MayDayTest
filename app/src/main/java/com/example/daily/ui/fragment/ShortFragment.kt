package com.example.daily.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daily.R
import com.example.daily.adapter.CommentsRvAdapter
import com.example.daily.viewModel.ShortViewModel

class ShortFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var shortViewModel: ShortViewModel
    private lateinit var adapter: CommentsRvAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView
    private var storyId: Long = 0

    companion object {
        fun newInstance(storyId: Long) : ShortFragment {
            val fragment = ShortFragment()
            val bundle = Bundle()
            bundle.putLong("storyId", storyId)
            fragment.arguments = bundle
            Log.d("ShortFragment", "storyId: $storyId")
            return fragment
        }
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        storyId = arguments?.getLong("storyId") ?: 0
//        shortViewModel = ViewModelProvider(this)[ShortViewModel::class.java]
//        Log.d("ID", " ${storyId}")
//        shortViewModel.loadShortComments(storyId)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("aaa", "onCreateView: ")
        return inflater.inflate(R.layout.fragment_short, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        storyId = arguments?.getLong("storyId") ?: 0
        shortViewModel = ViewModelProvider(this)[ShortViewModel::class.java]
        Log.d("ID", " ${storyId}")
        shortViewModel.loadShortComments(storyId)

        // 加载中状态
        shortViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        adapter = CommentsRvAdapter { commentId ->}
        recyclerView.adapter = adapter
        shortViewModel.shortComments.observe(viewLifecycleOwner) {comments ->
            adapter.submitList(comments)
            emptyView.visibility = if (comments.isEmpty()) View.VISIBLE else View.GONE
        }
    }
    fun initView(view: View) {
        recyclerView = view.findViewById(R.id.short_comments)
        progressBar = view.findViewById(R.id.short_progress_bar)
        emptyView = view.findViewById(R.id.empty_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}