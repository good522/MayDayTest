//package com.example.daily.ui.fragment
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.lifecycle.ViewModelProvider
//import com.bumptech.glide.Glide
//import com.example.daily.R
//import com.example.daily.bean.Story
//import com.example.daily.viewModel.StoryViewModel
//
//class StoryFragment : Fragment() {
//    private var story: Story? = null
//    private lateinit var viewModel: StoryViewModel
//
//
//    companion object {
//        //常量
//        private const val ARG_STORY_ID = "story_id"
//        fun newInstance(storyId: Int) : StoryFragment {
//            val fragment = StoryFragment()
//            val args = Bundle()
//            args.putInt(ARG_STORY_ID, storyId)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(requireParentFragment())[StoryViewModel::class.java]
//        val storyId = arguments?.getInt(ARG_STORY_ID) ?: 0
//        story = viewModel.getStoryByPosition(storyId)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_story, container, false)
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//          val title = view.findViewById<TextView>(R.id.story_title)
//          val hint = view.findViewById<TextView>(R.id.story_hint)
//          val content = view.findViewById<TextView>(R.id.story_item_text)
//          val firstImage = view.findViewById<ImageView>(R.id.first_image)
//          val storyImage = view.findViewById<ImageView>(R.id.story_item_image)
//          val commentsBtn = view.findViewById<Button>(R.id.comments_button)
//          val loveBtn = view.findViewById<Button>(R.id.love_button)
//          val transmitBtn = view.findViewById<Button>(R.id.transmit_button)
//          val collectBtn = view.findViewById<Button>(R.id.collect_button)
//
//          story?.let {
//              title.text = it.title
//              hint.text = it.hint
//              Glide.with(requireContext()).load(it.images.firstOrNull()).into(firstImage)
//              Glide.with(requireContext()).load(it.images.firstOrNull()).into(storyImage)
//          }
//    }
//}