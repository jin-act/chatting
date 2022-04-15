package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ai_caht.MainActivity
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import kotlinx.coroutines.delay
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
        btn.setEnabled(false)
        var hunger = MySharedPreferences.get_Hunger(ct).toInt()
        var stress = MySharedPreferences.get_Stress(ct).toInt()
        num1.isChecked = true
        lay1.setOnClickListener{
            if(btn.isEnabled == false){
                btn.setEnabled(true)
            }
            num1.isChecked = true
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = false
            sel = 1
        }
        lay2.setOnClickListener{
            if(btn.isEnabled == false){
                btn.setEnabled(true)
            }
            num1.isChecked = false
            num2.isChecked = true
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = false
            sel = 2
        }
        lay3.setOnClickListener{
            if(btn.isEnabled == false){
                btn.setEnabled(true)
            }
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = true
            num4.isChecked = false
            num5.isChecked = false
            sel = 3
        }
        lay4.setOnClickListener{
            if(btn.isEnabled == false){
                btn.setEnabled(true)
            }
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = true
            num5.isChecked = false
            sel = 4
        }
        lay5.setOnClickListener{
            if(btn.isEnabled == false){
                btn.setEnabled(true)
            }
            num1.isChecked = false
            num2.isChecked = false
            num3.isChecked = false
            num4.isChecked = false
            num5.isChecked = true
            sel = 5
        }

        btn.setOnClickListener {
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
                    MySharedPreferences.set_Hunger(ct,hunger.toString())
                    MySharedPreferences.set_Stress(ct,stress.toString())
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
                if(satisfied == 1){
                    txsatisfied.setText("!!!")
                }else{
                    txsatisfied.setText("...")
                }
                Timer().schedule(2000){
                    val intent = Intent(activity, PlayActivity::class.java)
                    startActivity(intent)
                }
            }else{
                txsatisfied.setText("""배가 불러요!!""")
            }
        }

        return view

    }

}