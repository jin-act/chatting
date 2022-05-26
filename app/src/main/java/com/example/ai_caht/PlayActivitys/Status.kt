package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.ai_caht.MainActivity
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import java.util.*
import kotlin.concurrent.timer


class status : Fragment() {
    var playActivity: PlayActivity? = null
    var timerTask: Timer? = null
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
        if (container != null) {
            ct = container.context
        }
        val view: View = inflater.inflate(R.layout.fragment_status, container, false) as ViewGroup
        var T_stress = view.findViewById<TextView>(R.id.textstress)
        var stress = view.findViewById<ProgressBar>(R.id.stress)
        var stat_view = view.findViewById<TextView>(R.id.Stat_TView)
        var stressbar = 0
        var hunger = 0
        var boredom = 0
        timerTask = timer(period = 1000) {
            if(playActivity?.stress == null){
                stressbar = 0
            }else{
                stressbar = playActivity!!.stress
            }
            if(playActivity?.hunger == null){
                hunger = 0
            }else{
                hunger = playActivity!!.hunger
            }
            if(playActivity?.boredom == null){
                boredom = 0
            }else{
                boredom = playActivity!!.boredom
            }
            println("new get")
            println("stress = " + stressbar + ", hunger = " + hunger + ", boredom = " + boredom)
            playActivity?.runOnUiThread{
                if(boredom >= 50 && hunger >= 50){
                    T_stress.setText("심심해하고 배고파하고 있어요")
                }else if(boredom >= 50){
                    T_stress.setText("심심해하고 있어요")
                }else if(hunger >= 50) {
                    T_stress.setText("배고파하고 있어요")
                }else{
                    T_stress.setText("상태가 아주 좋아요")
                }
                stat_view.setText("" + stressbar + "%")
                stress.setProgress(stressbar)
            }
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        timerTask?.cancel()
    }
}