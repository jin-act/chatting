package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import java.util.concurrent.ThreadLocalRandom

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
        var sel = 0
        var num = 0
        var num1 = view.findViewById<CheckBox>(R.id.no_1)
        var num2 = view.findViewById<CheckBox>(R.id.no_2)
        var num3 = view.findViewById<CheckBox>(R.id.no_3)
        var num4 = view.findViewById<CheckBox>(R.id.no_4)
        var num5 = view.findViewById<CheckBox>(R.id.no_5)
        var btn = view.findViewById<Button>(R.id.btn_sel)
        btn.setEnabled(false)
        var hunger = MySharedPreferences.get_Hunger(ct).toInt()
        var stress = MySharedPreferences.get_Stress(ct).toInt()
        num1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                if(btn.isEnabled == false){
                    btn.setEnabled(true)
                }
                num2.isChecked = false
                num3.isChecked = false
                num4.isChecked = false
                num5.isChecked = false
                sel = 1
            }
        }
        num2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                if(btn.isEnabled == false){
                    btn.setEnabled(true)
                }
                num1.isChecked = false
                num3.isChecked = false
                num4.isChecked = false
                num5.isChecked = false
                sel = 2
            }
        }
        num3.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                if(btn.isEnabled == false){
                    btn.setEnabled(true)
                }
                num1.isChecked = false
                num2.isChecked = false
                num4.isChecked = false
                num5.isChecked = false
                sel = 3
            }
        }
        num4.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                if(btn.isEnabled == false){
                    btn.setEnabled(true)
                }
                num1.isChecked = false
                num2.isChecked = false
                num3.isChecked = false
                num5.isChecked = false
                sel = 4
            }
        }
        num5.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                if(btn.isEnabled == false){
                    btn.setEnabled(true)
                }
                num1.isChecked = false
                num2.isChecked = false
                num3.isChecked = false
                num4.isChecked = false
                sel = 5
            }
        }
        btn.setOnClickListener {
            if(hunger >= 20){
                if(MySharedPreferences.get_food(ct) == "food")

                {
                    num = 4
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
                }else{
                    hunger -= 50
                    if(hunger <= 0 ) {
                        hunger = 0
                    }
                    println("hunger -> " + hunger)
                    MySharedPreferences.set_Hunger(ct,hunger.toString())
                }
                num = ThreadLocalRandom.current().nextInt(1,5)
                MySharedPreferences.set_food(ct,num.toString())

            }else if(sel == 0){

            }else{
                // 배부를 때 코드
            }
        }

        return view

    }

}