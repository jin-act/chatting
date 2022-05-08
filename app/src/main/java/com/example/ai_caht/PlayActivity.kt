package com.example.ai_caht

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.ai_caht.PlayActivitys.*
import com.example.ai_caht.TimeCheck.ForecdTerminationService
import com.example.ai_caht.test.RetrofitClient
import com.example.ai_caht.test.state.ParrotState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer

open class PlayActivity : AppCompatActivity() {
    var mBackWait:Long = 0
    var hunger = 0
    var stress = 0
    var boredom = 0
    var level = 0
    var affection = 0
    var counter = 0.0
    val C_time = 2 // 상태가 1증가하는 시간 실제 작동 시간은 15분으로 구상중이며, 배고픔이나 지루함 기준으로 0에서 100까지 1500분 즉 25시간이 필요하다.
    var result : Long = 0
    var TimeOfChangeCondition = 0
    var TOCC_Counter : Long = 0
    var timerTask: Timer? = null
    //변수 넘기는 방법 실험
    companion object {
        lateinit var instance : PlayActivity
        private set
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        instance = this

        //최초 실행 작동
        isFirst()
        //어플 최종 종료시간과 현재 시간의 차를 계산

        var Fmanager : FragmentManager = supportFragmentManager
        var FTransaction : FragmentTransaction = Fmanager.beginTransaction()
        var Last_Counter : Double = 0.0
        var feed = findViewById<LinearLayout>(R.id.feed)
        var feedimg = findViewById<ImageView>(R.id.feedimg)
        var memory = findViewById<LinearLayout>(R.id.memory)
        var status = findViewById<LinearLayout>(R.id.status)
        var statusimg = findViewById<ImageView>(R.id.statusimg)
        var conver = findViewById<LinearLayout>(R.id.conver)
        var converimg = findViewById<ImageView>(R.id.converimg)
        var account = findViewById<ImageView>(R.id.account)
        val brush = findViewById<LinearLayout>(R.id.memory)
        val brushimg = findViewById<ImageView>(R.id.brushimg)
        val check  = MySharedPreferences.get_finish(this)
        //상태의 수치를 변경할 때 사용
        //저장되어있는 상태 호출

        var setting = findViewById<ImageView>(R.id.setting)

        //기능 변경 *********통신으로 받기***********
        var current_time = System.currentTimeMillis()
        var last_time : Long = 0


        var userId = MySharedPreferences.getUserId(this)
        var retrofitClient = RetrofitClient.getInstance()
        var initMyApi = RetrofitClient.getRetrofitInterface()
        initMyApi.getParrotState(userId)
            .enqueue(object : Callback<ParrotState> {
                override fun onResponse(
                    call: Call<ParrotState>,
                    response: Response<ParrotState>
                ) {
                    // 통신으로 상태 받아오기
                    var body = response.body()
                    if(body!!.hunger != null)
                    {
                        hunger = body!!.hunger.toInt()
                        stress = body!!.stress.toInt()
                        boredom = body!!.boredom.toInt()
                        affection = body!!.affection.toInt()
                        level = body!!.level.toInt()
                        Last_Counter = body!!.counter.toDouble()
                        last_time = body!!.lastTime

                    }
                    else{
                        hunger = 0
                        stress = 0
                        boredom = 0
                        affection = 0
                        level = 0
                        Last_Counter = 0.0
                        last_time = current_time
                    }
                    result = (current_time - last_time)/1000
                    println("result -> " + result)
                    TimeOfChangeCondition = (result/C_time).toInt()
                    println("TOCC1 ->" + TimeOfChangeCondition)
                    TOCC_Counter = result%C_time
                    println("L_counter -> " + Last_Counter)
                    //상태의 수치 변경
                    counter = TOCC_Counter + Last_Counter
                    println("counter -> " + counter)

                    if(counter > C_time){
                        TimeOfChangeCondition = TimeOfChangeCondition + (counter / C_time).toInt()
                        counter = counter%C_time
                    }
                    println("TOCC2 ->" + TimeOfChangeCondition)
                    println("counter -> " + counter)
                    //10분이 되면 허기와 무료함 1상승 만약 허기와 무료함이 50이상일 때 스트레스도 동시에 1상승 두 조건이 동시에 만족하면 2상승
                    hunger = calcul(hunger,TimeOfChangeCondition)
                    println("hunger " + hunger)
                    boredom = calcul(boredom,TimeOfChangeCondition)
                    stress = cal_stress(stress,hunger,boredom,TimeOfChangeCondition)
                    save()

                    println(body)
                }
                override fun onFailure(call: Call<ParrotState>, t: Throwable) {
                    println(t.message)
                }

            })
        //***************************************

        changeFragment(2)
        //흐름 계산
        timerTask = timer(period = 1000){
            counter++
            //reset()
            if(counter >= C_time){
                hunger = calcul(hunger,1)
                boredom = calcul(boredom,1)
                stress = cal_stress(stress,hunger,boredom,1)
                counter = 0.0
                println("status = h," + hunger + ", " + boredom + ", " + stress)


            }
            save()
        }

        if(check == "true"){
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, MemoryFragment())
                    .commit()
        }

        account.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.list,list())
                    .commit()
        })



        feed.setOnClickListener({
            changeFragment(1)
            feedimg.setImageResource(R.drawable.underbar_forknife1)
            statusimg.setImageResource(R.drawable.underbar_dashboard)
            brushimg.setImageResource(R.drawable.underbar_brush)

        })

        status.setOnClickListener({
            changeFragment(2)
            statusimg.setImageResource(R.drawable.underbar_dashboard1)
            feedimg.setImageResource(R.drawable.underbar_forknife)
            brushimg.setImageResource(R.drawable.underbar_brush)

        })

        memory.setOnClickListener({
            changeFragment(3)
            statusimg.setImageResource(R.drawable.underbar_dashboard)
            feedimg.setImageResource(R.drawable.underbar_forknife)
            brushimg.setImageResource(R.drawable.underbar_brush1)

        })


        conver.setOnClickListener({
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        })



        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.test3).into(ani1)
    }

    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
        } else {
            MySharedPreferences.set_finish(this, "false")
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
            MySharedPreferences.set_food(this, "1")
            MySharedPreferences.set_finish(this, "false")

            editor.putBoolean("isFirst", true)
            editor.commit()
        }
    }

    override fun onStop() {
        super.onStop()
        save()
        savedata()
        println("stop!!")
        timerTask?.cancel()
    }
    /*fun reset() {
        hunger = MySharedPreferences.get_Hunger(this).toInt()
        stress = MySharedPreferences.get_Stress(this).toInt()
        boredom = MySharedPreferences.ger_Boredom(this).toInt()
        affection = MySharedPreferences.get_Affection(this).toInt()
        level = MySharedPreferences.get_Level(this).toInt()
    }*/
    fun save() {
        var condition = (hunger + stress*1000 + boredom*1000000 + affection*1000000000 +
                level*1000000000000 + counter*1000000000000000).toLong()
        println("this -> "+condition)
        var intent_condition = Intent(this, ForecdTerminationService::class.java)
        intent_condition.putExtra("condition", condition)
        startService(intent_condition)
    }
    fun savedata() {
        //더 좋은 방법을 생각하기 현재 -> 매 순간 sharedpreferences를 최신화하여 저장
        MySharedPreferences.set_condition(this,hunger.toString(),boredom.toString(),stress.toString(),affection.toString(),level.toString(),counter.toString())
        var current_time = System.currentTimeMillis()
        MySharedPreferences.set_Time(this, current_time)
        var userId = MySharedPreferences.getUserId(this)
        var parrotstate =
            ParrotState(hunger.toString(), stress.toString(), boredom.toString(), affection.toString(), level.toString(), counter.toString(), current_time)
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
        timerTask = timer(period = 1000){
            counter++
            //reset()
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
    fun changeFragment(index: Int){
        when(index){
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, feed())
                    .commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, status())
                    .commit()
            }
            3 -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.infospace, MemoryFragment())
                        .commit()
            }

        }
    }
}