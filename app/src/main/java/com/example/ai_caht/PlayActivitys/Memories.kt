package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import java.util.*

class Memories : Fragment() {
    var playActivity: PlayActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        playActivity = activity as PlayActivity?
    }

    override fun onDetach() {
        super.onDetach()
        playActivity = null
    }

    var ct: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (container != null) {
            ct = container.context
        }
        val view: View = inflater.inflate(R.layout.fragment_memories, container, false) as ViewGroup
        // Inflate the layout for this fragment
        return view
    }
}