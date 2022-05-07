package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R


class MemoryFragment : Fragment() {
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (container != null) {
            ct = container.context
        }
        val view: View = inflater.inflate(R.layout.fragment_memory, container, false) as ViewGroup
        var btn = view.findViewById<FrameLayout>(R.id.btn_draw)

        btn.setOnClickListener {
            val intent = Intent(activity, DrawActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}