package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.example.ai_caht.*
import kotlinx.coroutines.delay
import org.w3c.dom.Text
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.schedule

class feed : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_feed, container, false) as ViewGroup
        var sel = 1
        var num = 0
        var satisfied = 0
        var num1 = view.findViewById<CheckBox>(R.id.no_1)
        var num2 = view.findViewById<CheckBox>(R.id.no_2)
        var num3 = view.findViewById<CheckBox>(R.id.no_3)
        var num4 = view.findViewById<CheckBox>(R.id.no_4)
        var num5 = view.findViewById<CheckBox>(R.id.no_5)
        var lay1 = view.findViewById<LinearLayout>(R.id.lay_1)
        var lay2 = view.findViewById<LinearLayout>(R.id.lay_2)
        var lay3 = view.findViewById<LinearLayout>(R.id.lay_3)
        var lay4 = view.findViewById<LinearLayout>(R.id.lay_4)
        var lay5 = view.findViewById<LinearLayout>(R.id.lay_5)
        var btn = view.findViewById<Button>(R.id.btn_sel)
        var txsatisfied = view.findViewById<TextView>(R.id.Feed_hint)
        var text_condition = view.findViewById<TextView>(R.id.text_condition)
        var hunger : Int
        var stress : Int
        if(playActivity?.hunger == null){
            hunger=0
            btn.setEnabled(false)
        }else
        {
            hunger = playActivity!!.hunger
        }
        println("hunger -> " + hunger)
        setText(text_condition,hunger,btn)
        num1.isChecked = true
        lay1.setOnClickListener{
            num1.isChecked = true
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = false
            sel = 1
        }
        lay2.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = true
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = false
            sel = 2
        }
        lay3.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = true
            num4.isChecked = false
            num5.isChecked = false
            sel = 3
        }
        lay4.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = true
            num5.isChecked = false
            sel = 4
        }
        lay5.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = true
            sel = 5
        }
        num1.setOnClickListener{
            num1.isChecked = true
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = false
            sel = 1
        }
        num2.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = true
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = false
            sel = 2
        }
        num3.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = true
            num4.isChecked = false
            num5.isChecked = false
            sel = 3
        }
        num4.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = true
            num5.isChecked = false
            sel = 4
        }
        num5.setOnClickListener{
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = true
            sel = 5
        }
        btn.setOnClickListener {
            if(playActivity?.hunger == null){
                hunger = 0
            }else{
                hunger = playActivity!!.hunger
            }
            if(playActivity?.stress == null){
                stress = 0
            }else{
                stress = playActivity!!.stress
            }
            if(hunger >= 20){
                if(MySharedPreferences.get_food(ct) == "food")

                {
                    num = ThreadLocalRandom.current().nextInt(1,5)
                }else{
                    num = MySharedPreferences.get_food(ct).toInt()
                }
                println("want food -> " + Want_food.want_food(num))
                if(num == sel){
                    hunger -= 50
                    stress -= 10
                    if(hunger <= 0 ) {
                        hunger = 0
                    }
                    if(stress <= 0){
                        stress = 0
                    }
                    println("hunger -> " + hunger)
                    println("stress -> " + stress)
                    satisfied = 1
                }else{
                    hunger -= 50
                    if(hunger <= 0 ) {
                        hunger = 0
                    }
                    println("hunger -> " + hunger)
                    MySharedPreferences.set_Hunger(ct,hunger.toString())
                    satisfied = 0
                }
                num = ThreadLocalRandom.current().nextInt(1,5)
                MySharedPreferences.set_food(ct,num.toString())
                //*****************알림창 추가******************
                if(satisfied == 1){

                }else{

                }
                //********************************************

                playActivity?.hunger = hunger
                playActivity?.stress = stress
                playActivity?.changeFragment(2)
                var statusimg = playActivity?.findViewById<ImageView>(R.id.statusimg)
                var feedimg = playActivity?.findViewById<ImageView>(R.id.feedimg)
                statusimg?.setImageResource(R.drawable.underbar_parrot1)
                feedimg?.setImageResource(R.drawable.underbar_food)
                playActivity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.list, FeeddialogFragment())
                    ?.commit()
            }
        }

        return view

    }
    fun setText(text_condition : TextView, hunger : Int, btn : Button){
        var late = 20
        var text_late = ""
        if(hunger >= 20){
            btn.setEnabled(true)
            btn.setBackgroundResource(R.drawable.okay_button)
            text_condition.setText("앵무가 좋아하는 음식을 줘보세요")
        }
        else{
            btn.setEnabled(false)
            btn.setBackgroundResource(R.drawable.contents_box12)
            late = 20-hunger
            if(late > 0 && late <= 4){
                text_late = "지금은 배가 좀 부른것 같아요"
            }else if(late > 4 && late <= 8){
                text_late = "배불러 보여요"
            }else if(late > 8 && late <= 12){
                text_late = "배가 많이 부른것 같아요"
            }else if(late > 12 && late <= 16){
                text_late = "배가 너무 불러서 힘들어 보여요"
            }else{
                text_late = "음식을 보는 것만으로도 괴로워 보여요"
            }
            var current_time = System.currentTimeMillis()
            text_condition.setText(text_late)
        }
    }
}