package com.example.ai_caht

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.ai_caht.PlayActivitys.*
import com.example.ai_caht.TimeCheck.ForecdTerminationService
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.timer

class PlayActivity : AppCompatActivity() {
    var mBackWait:Long = 0
    var hunger = 0
    var stress = 0
    var boredom = 0
    var level = 0
    var affection = 0
    var counter = 0.0
    val C_time = 2
    var timerTask: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        //최초 실행 작동
        isFirst()
        //어플 최종 종료시간과 현재 시간의 차를 계산



        var feed = findViewById<LinearLayout>(R.id.feed)
        var feedimg = findViewById<ImageView>(R.id.feedimg)
        var status = findViewById<LinearLayout>(R.id.status)
        var statusimg = findViewById<ImageView>(R.id.statusimg)
        var conver = findViewById<LinearLayout>(R.id.conver)
        var converimg = findViewById<ImageView>(R.id.converimg)
        var account = findViewById<ImageView>(R.id.account)

        var current_time = System.currentTimeMillis()
        var last_time = MySharedPreferences.get_Time(this)
        var result = (current_time - last_time)/1000
        println("result ->" + result)
        var TimeOfChangeCondition = (result/C_time).toInt()
        println("TOCC ->" + TimeOfChangeCondition)
        var TOCC_Counter = result%C_time
        println("TOCC_C ->" + TOCC_Counter)
        //상태의 수치를 변경할 때 사용
        //저장되어있는 상태 호출


        //기능 변경 *********통신으로 받기***********
        hunger = MySharedPreferences.get_Hunger(this).toInt()
        println("hunger " + hunger)
        stress = MySharedPreferences.get_Stress(this).toInt()
        boredom = MySharedPreferences.ger_Boredom(this).toInt()
        affection = MySharedPreferences.get_Affection(this).toInt()
        level = MySharedPreferences.get_Level(this).toInt()
        var Last_Counter = MySharedPreferences.get_Counter(this)
        //***************************************

        println("L_counter -> " + Last_Counter)
        //상태의 수치 변경
        counter = TOCC_Counter + Last_Counter.toDouble()
        println("counter -> " + counter)

        if(counter > C_time){
            TimeOfChangeCondition = TimeOfChangeCondition + (counter / C_time).toInt()
            counter = counter%C_time
        }
        println("TOCC ->" + TimeOfChangeCondition)
        println("counter -> " + counter)
                //10분이 되면 허기와 무료함 1상승 만약 허기와 무료함이 50이상일 때 스트레스도 동시에 1상승 두 조건이 동시에 만족하면 2상승
        hunger = calcul(hunger,TimeOfChangeCondition)
        println("hunger " + hunger)
        boredom = calcul(boredom,TimeOfChangeCondition)
        stress = cal_stress(stress,hunger,boredom,TimeOfChangeCondition)
        save()
        supportFragmentManager.beginTransaction()
            .replace(R.id.infospace, status())
            .commit()
        //흐름 계산
        timerTask = timer(period = 1000){
            counter++
            reset()
            if(counter >= C_time){
                hunger = calcul(hunger,1)
                boredom = calcul(boredom,1)
                stress = cal_stress(stress,hunger,boredom,1)
                counter = 0.0
                println("status = h," + hunger + ", " + boredom + ", " + stress)


            }
            save()
        }

        account.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.list,list())
                    .commit()
        })



        feed.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, feed())
                    .commit()
            feedimg.setImageResource(R.drawable.underbar_food1)
            statusimg.setImageResource(R.drawable.underbar_parrot)

        })

        status.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, status())
                    .commit()
            statusimg.setImageResource(R.drawable.underbar_parrot1)
            feedimg.setImageResource(R.drawable.underbar_food1)

        })


        conver.setOnClickListener({
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        })


        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.ani1).into(ani1)
    }
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
        } else {
            ActivityCompat.finishAffinity(this);
        }
    }
    fun calcul(Item : Int, TimeOfChangeCondition : Int) : Int{
        var item = Item
        item += TimeOfChangeCondition

        if(item > 100){
            item = 100
        }
        return item
    }
    fun cal_stress(Stress : Int, Hunger : Int, Boredom : Int, TimeOfChangeCondition: Int) : Int{
        var stress = Stress
        //분기점 50 계산
        var hunger = (Hunger - 50)
        var boredom = (Boredom - 50)
        var TOCC_H = hunger - TimeOfChangeCondition
        var TOCC_B = boredom - TimeOfChangeCondition
        if(TOCC_B <= 0 && TimeOfChangeCondition + TOCC_B >= 0){
            stress += TimeOfChangeCondition + TOCC_B
        }else if(TOCC_B > 0){
            stress += TimeOfChangeCondition
        }

        if(TOCC_H <= 0 && TimeOfChangeCondition + TOCC_H >= 0){
            stress += TimeOfChangeCondition + TOCC_H
        }else if(TOCC_H > 0){
            stress += TimeOfChangeCondition
        }

        if(stress > 100){
            stress = 100
        }
        return stress
    }
    fun isFirst(){
        val pref : SharedPreferences = this.getSharedPreferences(MySharedPreferences.MY_ACCOUNT, Context.MODE_PRIVATE)
        var first =  pref.getBoolean("isFirst", false)
        //최초 실행이라면
        if(first == false) {
            val editor : SharedPreferences.Editor = pref.edit()
            MySharedPreferences.set_food(this,"1")

            //최초 실행 시 작동 부분
            // 현재 시간 저장
            var current_time = System.currentTimeMillis()
            var time = current_time
            MySharedPreferences.set_Time(this, time)

            //앵무의 상태 초기화
            MySharedPreferences.set_condition(this, "0", "0", "0", "0", "0", "0")
            MySharedPreferences.set_food(this, "food")

            editor.putBoolean("isFirst", true)
            editor.commit()
        }
    }

    override fun onStop() {
        super.onStop()
        save()
        println("stop!!")
        timerTask?.cancel()
    }
    fun reset() {
        hunger = MySharedPreferences.get_Hunger(this).toInt()
        stress = MySharedPreferences.get_Stress(this).toInt()
        boredom = MySharedPreferences.ger_Boredom(this).toInt()
        affection = MySharedPreferences.get_Affection(this).toInt()
        level = MySharedPreferences.get_Level(this).toInt()
    }
    fun save() {
        var condition = (hunger + stress*1000 + boredom*1000000 + affection*1000000000 +
                level*1000000000000 + counter*1000000000000000).toLong()
        println("this -> "+condition)
        var intent_condition = Intent(this, ForecdTerminationService::class.java)
        intent_condition.putExtra("condition", condition)
        startService(intent_condition)
        //더 좋은 방법을 생각하기 현재 -> 매 순간 sharedpreferences를 최신화하여 저장
        MySharedPreferences.set_condition(this,hunger.toString(),boredom.toString(),stress.toString(),affection.toString(),level.toString(),counter.toString())
        print("save")
    }

    override fun onRestart() {
        super.onRestart()
        var current_time = System.currentTimeMillis()
        var last_time = MySharedPreferences.get_Time(this)
        var result = (current_time - last_time)/1000
        var Last_Counter = MySharedPreferences.get_Counter(this)
        var TimeOfChangeCondition = (result/C_time).toInt()
        println("TOCC ->" + TimeOfChangeCondition)
        var TOCC_Counter = result%C_time
        println("TOCC_C ->" + TOCC_Counter)

        println("L_counter -> " + Last_Counter)
        //상태의 수치 변경
        counter = TOCC_Counter + Last_Counter.toDouble()
        println("counter -> " + counter)

        if(counter > C_time){
            TimeOfChangeCondition = TimeOfChangeCondition + (counter / C_time).toInt()
            counter = counter%C_time
        }
        println("TOCC ->" + TimeOfChangeCondition)
        println("counter -> " + counter)
        //10분이 되면 허기와 무료함 1상승 만약 허기와 무료함이 50이상일 때 스트레스도 동시에 1상승 두 조건이 동시에 만족하면 2상승
        hunger = calcul(hunger,TimeOfChangeCondition)
        println("hunger " + hunger)
        boredom = calcul(boredom,TimeOfChangeCondition)
        stress = cal_stress(stress,hunger,boredom,TimeOfChangeCondition)
        save()
        supportFragmentManager.beginTransaction()
            .replace(R.id.infospace, status())
            .commitAllowingStateLoss()
        timerTask = timer(period = 1000){
            counter++
            reset()
            if(counter >= C_time){
                hunger = calcul(hunger,1)
                boredom = calcul(boredom,1)
                stress = cal_stress(stress,hunger,boredom,1)
                counter = 0.0
                println("status = h," + hunger + ", " + boredom + ", " + stress)
            }
            save()
        }
    }
}