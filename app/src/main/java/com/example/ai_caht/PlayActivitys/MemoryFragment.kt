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
import com.bumptech.glide.Glide
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import java.util.*
import kotlin.concurrent.timer


class MemoryFragment : Fragment() {
    var playActivity: PlayActivity? = null
    var hunger: Int = 0
    var boredem: Int = 0
    var stress: Int = 0

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
        var ani1 = (activity as PlayActivity).findViewById<ImageView>(R.id.ani1)
        hunger = MySharedPreferences.get_Hunger(context).toInt()
        boredem = MySharedPreferences.ger_Boredom(context).toInt()
        stress = MySharedPreferences.get_Stress(context).toInt()

        if(check == "true"){
            layout.visibility = View.VISIBLE
            val image = MySharedPreferences.get_image(activity)
            if(image.equals("통신에러")){
                text.text = "통신에러 다시 시도해주세요"
            }
            else {
                text.text = "정답은 " + image + "!"
            }
            btn.setBackgroundColor(Color.parseColor("#777777"))
            text_color.setTextColor(Color.parseColor("#aaaaaa"))
            btn.isClickable = false
        }


        ok_btn.setOnClickListener {
            Toast.makeText(context, "정답", Toast.LENGTH_SHORT).show()
            // 상태 변경
            playActivity?.stress = 0
            playActivity?.boredom = 0
            var timerTask: Timer? = null
            var time = 0
            timerTask = timer(period = 10) {	// timer() 호출
                time++	// period=10, 0.01초마다 time를 1씩 증가
                val sec = (time / 100) + 1	// time/100, 나눗셈의 몫 (초 부분)
                val Ltime = 2.5-sec
                var changed = 0
                if(sec >= 2.5){
                    timerTask?.cancel()
                }
                // UI조작을 위한 메서드
                playActivity?.runOnUiThread {
                    if(changed == 0){
                        Glide.with(ct!!).load(R.raw.glad).into(ani1)
                        changed = 1
                        println("timer running")
                    }

                }
            }
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                if(stress >= 50){
                    Glide.with(ct!!).load(R.raw.angry2).into(ani1)
                }else if(hunger >= 50){
                    Glide.with(ct!!).load(R.raw.angry1).into(ani1)
                }else if(boredem >= 50){
                    Glide.with(ct!!).load(R.raw.sleep).into(ani1)
                }else{
                    Glide.with(ct!!).load(R.raw.stand).into(ani1)
                }
            }, 2500)
            MySharedPreferences.set_finish(context, "false")
            layout.visibility = View.GONE
            text.text = "' 내가 말한걸 그려주면 맞춰 볼께'"
            btn.setBackgroundColor(Color.parseColor("#ffb830"))
            text_color.setTextColor(Color.parseColor("#FFFFFF"))
            btn.isClickable = true
        }

        no_btn.setOnClickListener {
            Toast.makeText(context, "오답", Toast.LENGTH_SHORT).show()
            // 상태 변경
            playActivity?.stress = 0
            playActivity?.boredom = 0
            var timerTask: Timer? = null
            var time = 0
            timerTask = timer(period = 10) {	// timer() 호출
                time++	// period=10, 0.01초마다 time를 1씩 증가
                val sec = (time / 100) + 1	// time/100, 나눗셈의 몫 (초 부분)
                val Ltime = 2.5-sec
                var changed = 0
                if(sec >= 2.5){
                    timerTask?.cancel()
                }
                // UI조작을 위한 메서드
                playActivity?.runOnUiThread {
                    if(changed == 0){
                        Glide.with(ct!!).load(R.raw.embressed).into(ani1)
                        changed = 1
                        println("timer running")
                    }

                }
            }
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                if(stress >= 50){
                    Glide.with(ct!!).load(R.raw.angry2).into(ani1)
                }else if(hunger >= 50){
                    Glide.with(ct!!).load(R.raw.angry1).into(ani1)
                }else if(boredem >= 50){
                    Glide.with(ct!!).load(R.raw.sleep).into(ani1)
                }else{
                    Glide.with(ct!!).load(R.raw.stand).into(ani1)
                }
            }, 2500)
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