package com.example.ai_caht.TimeCheck

import android.app.Service
import android.widget.Toast

import android.util.Log

import android.content.Intent

import android.os.IBinder
import androidx.annotation.Nullable
import com.example.ai_caht.PlayActivitys.MySharedPreferences
import java.util.*
import com.example.ai_caht.MainActivity
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.test.RetrofitClient
import com.example.ai_caht.test.state.ParrotState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForecdTerminationService : Service() {
    var hunger: String = ""
    var stress: String = ""
    var boredom: String = ""
    var affection: String = ""
    var level: String = ""
    var counter: String = ""
    var condition: String = ""

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var play: PlayActivity
        val data = intent?.getLongExtra("condition", 0)!!
        println("****calculate****")
        println(data)
        var Counter = (data / 1000000000000000).toInt()
        println("C : " + Counter)
        var Level = ((data - Counter * 1000000000000000) / 1000000000000).toInt()
        println("L : " + Level)
        var Affection =
            ((data - Counter * 1000000000000000 - Level * 1000000000000) / 1000000000).toInt()
        println("A : " + Affection)
        var Boredom =
            ((data - Counter * 1000000000000000 - Level * 1000000000000 - Affection * 1000000000) / 1000000).toInt()
        println("B : " + Boredom)
        var Stress =
            ((data - Counter * 1000000000000000 - Level * 1000000000000 - Affection * 1000000000 - Boredom * 1000000) / 1000).toInt()
        println("S : " + Stress)
        var Hunger =
            ((data - Counter * 1000000000000000 - Level * 1000000000000 - Affection * 1000000000 - Boredom * 1000000 - Stress * 1000)).toInt()
        println("H : " + Hunger)


        hunger = Hunger.toString()
        stress = Stress.toString()
        boredom = Boredom.toString()
        affection = Affection.toString()
        level = Level.toString()
        counter = Counter.toString()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent) { //핸들링 하는 부분
        //현재 시간 저장
        var current_time = System.currentTimeMillis()
        MySharedPreferences.set_Time(this, current_time)
        println("set_time" + current_time)
        Toast.makeText(this, "onTaskRemoved ", Toast.LENGTH_SHORT).show()
        //현재 상태 저장 -> 1. 수치 저장 0~100, 2.경과 시간 0~599 -> 0초에서 10분까지 10분이 되면 허기와 무료함 1상승 만약 허기와 무료함이 50이상일 때 스트레스도 동시에 1상승 두 조건이 동시에 만족하면 2상승


        //해당 로그인의 데이터 값
        //기능 변경 *******통신으로 보내기********
        MySharedPreferences.set_condition(this, hunger, boredom, stress, affection, level, counter)
        var userId = MySharedPreferences.getUserId(this)
        var parrotstate =
            ParrotState(hunger, stress, boredom, affection, level, counter, current_time)
        var retrofitClient = RetrofitClient.getInstance()
        var initMyApi = RetrofitClient.getRetrofitInterface()
        initMyApi.sendParrotState(userId, parrotstate)
            .enqueue(object : Callback<ParrotState> {
                override fun onResponse(
                    call: Call<ParrotState>,
                    response: Response<ParrotState>
                ) {
                    var body = response.body()
                    println(body)
                }

                override fun onFailure(call: Call<ParrotState>, t: Throwable) {
                    println(t.message)
                }

            })
        //************************************
        stopSelf() //서비스 종료
    }
}
