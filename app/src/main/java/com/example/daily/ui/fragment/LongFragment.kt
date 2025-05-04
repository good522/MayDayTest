package com.example.daily.ui.fragment

import android.os.Bundle
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
import com.example.daily.viewModel.LongViewModel

class LongFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var longViewModel: LongViewModel
    private lateinit var adapter: CommentsRvAdapter
    private lateinit var emptyView: TextView
    private lateinit var progressBar: ProgressBar
    private var storyId: Long = 0

    companion object {
        fun newInstance(storyId: Long) : LongFragment {
            val fragment = LongFragment()
            val bundle = Bundle()
            bundle.putLong("storyId", storyId)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyId = arguments?.getLong("storyId") ?: 0
        longViewModel = ViewModelProvider(this)[LongViewModel::class.java]
        longViewModel.loadLongComments(storyId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_long, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.long_comments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        emptyView = view.findViewById(R.id.empty_view)
        progressBar = view.findViewById(R.id.long_progress_bar)

        // 加载中状态
        longViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        adapter = CommentsRvAdapter {commentId ->}
        recyclerView.adapter = adapter
        longViewModel.longComments.observe(viewLifecycleOwner) {comments ->
            adapter.submitList(comments)
            emptyView.visibility = if (comments.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}