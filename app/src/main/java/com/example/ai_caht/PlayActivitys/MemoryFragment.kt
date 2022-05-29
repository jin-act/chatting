package com.example.ai_caht.PlayActivitys

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import java.util.*


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
    @SuppressLint("SetTextI18n")
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
        var text = view.findViewById<TextView>(R.id.text_dog)
        var text_color = view.findViewById<TextView>(R.id.text_color)

        if(check == "true"){
            layout.visibility = View.VISIBLE
            val image = MySharedPreferences.get_image(activity)
            text.text = "정답은 " +image+ "!"
            btn.setBackgroundColor(Color.parseColor("#777777"))
            text_color.setTextColor(Color.parseColor("#aaaaaa"))
            btn.isClickable = false
        }


        ok_btn.setOnClickListener {
            Toast.makeText(context, "정답", Toast.LENGTH_SHORT).show()
            MySharedPreferences.set_finish(context, "false")
            layout.visibility = View.GONE
            text.text = "' 내가 말한걸 그려주면 맞춰 볼께'"
            btn.setBackgroundColor(Color.parseColor("#ffb830"))
            text_color.setTextColor(Color.parseColor("#FFFFFF"))
            btn.isClickable = true
        }

        no_btn.setOnClickListener {
            Toast.makeText(context, "오답", Toast.LENGTH_SHORT).show()
            MySharedPreferences.set_finish(context, "false")
            layout.visibility = View.GONE
            text.text = "' 내가 말한걸 그려주면 맞춰 볼께'"
            btn.setBackgroundColor(Color.parseColor("#ffb830"))
            text_color.setTextColor(Color.parseColor("#FFFFFF"))
            btn.isClickable = true
        }

        btn.setOnClickListener {
            val random = Random()
            val num = random.nextInt(4)
            MySharedPreferences.set_type(context, num.toString())
            val intent = Intent(activity, DrawActivity::class.java)
            startActivity(intent)
            MySharedPreferences.set_finish(context, "false")
        }
        return view
    }
}