package com.example.ai_caht

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer
import android.graphics.Color
import com.bumptech.glide.Glide
import com.example.ai_caht.Login.login


class MainActivity : AppCompatActivity() {
    var check = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
                .replace(R.id.login_join, login())
                .commit()

        val login = findViewById<TextView>(R.id.login)
        val join = findViewById<TextView>(R.id.join)

        login.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.login_join, com.example.ai_caht.Login.login())
                    .commit()
            login.setTextColor(Color.rgb(255,168,39));
            join.setTextColor(Color.rgb(160,160,160));

        })

        join.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.login_join, com.example.ai_caht.Login.join())
                    .commit()
            join.setTextColor(Color.rgb(255,168,39));
            login.setTextColor(Color.rgb(160,160,160));
        })

    }
    private fun start() {
        var timerTask: Timer? = null
        var time = 0
        val text_Timer = findViewById<TextView>(R.id.text_timer)
        timerTask = timer(period = 10) {	// timer() 호출
            time++	// period=10, 0.01초마다 time를 1씩 증가
            val sec = (time / 100) + 1	// time/100, 나눗셈의 몫 (초 부분)
            val Ltime = 5-sec
            if(sec >= 4){
                timerTask?.cancel()
            }
            // UI조작을 위한 메서드
            runOnUiThread {
                text_Timer.text = "5회 잘못 입력되어 $Ltime 초 로그인이 제한됩니다."	// TextView 세팅
            }
        }
        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.ani1).into(ani1)

    }

}