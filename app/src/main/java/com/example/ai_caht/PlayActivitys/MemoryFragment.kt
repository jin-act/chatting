package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
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
        val check = MySharedPreferences.get_finish(context)
        val view: View = inflater.inflate(R.layout.fragment_memory, container, false) as ViewGroup
        var ok_btn = view.findViewById<Button>(R.id.ok)
        var no_btn = view.findViewById<Button>(R.id.no)
        var btn = view.findViewById<FrameLayout>(R.id.btn_draw)
        var layout = view.findViewById<LinearLayout>(R.id.layout_memory)

        if(check == "true"){
            layout.visibility = View.VISIBLE
        }

        ok_btn.setOnClickListener {
            Toast.makeText(context, "정답", Toast.LENGTH_SHORT).show()
            MySharedPreferences.clear_finish(context)
            layout.visibility = View.GONE
        }

        no_btn.setOnClickListener {
            Toast.makeText(context, "오답", Toast.LENGTH_SHORT).show()
            MySharedPreferences.clear_finish(context)
            layout.visibility = View.GONE
        }

        btn.setOnClickListener {
            val intent = Intent(activity, DrawActivity::class.java)
            startActivity(intent)
            MySharedPreferences.clear_finish(context)
        }
        return view
    }


}