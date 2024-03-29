package com.example.ai_caht

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.ai_caht.PlayActivitys.*
import com.example.ai_caht.TimeCheck.ForecdTerminationService
import com.example.ai_caht.test.Record.PageSize
import com.example.ai_caht.test.Record.ParrotRecord
import com.example.ai_caht.test.RetrofitClient
import com.example.ai_caht.test.state.ParrotState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
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
    val C_time = 1 // 상태가 1증가하는 시간 실제 작동 시간은 15분으로 구상중이며, 배고픔이나 지루함 기준으로 0에서 100까지 1500분 즉 25시간이 필요하다.
    var result : Long = 0
    var TimeOfChangeCondition = 0
    var TOCC_Counter : Long = 0
    var timerTask: Timer? = null
    var P_state = 0
    var last_state = 0
    var pageSize = 1
    var date = "0000-00-00"
    var pState = 0
    var feedCount = 0
    var feedKind = 0
    var playType = 0
    var playResult = 0
    var chatCount = 0
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
        var BG = findViewById<LinearLayout>(R.id.BG)
        val check  = MySharedPreferences.get_finish(this)
        var current_time = System.currentTimeMillis()
        var last_time : Long = 0
        val HOUR = SimpleDateFormat("H")
        var H = HOUR.format(current_time).toInt()
        println("current time = " + H)
        if(H > 3 && H <= 8){
            BG.setBackgroundResource(R.drawable.background_down2)
        }else if(H > 8 && H <= 16){
            BG.setBackgroundResource(R.drawable.background_afternoon2)
        }else if(H > 16 && H <= 18){
            BG.setBackgroundResource(R.drawable.background_twilight)
        }else{
            BG.setBackgroundResource(R.drawable.background_night2)
        }

        //상태의 수치를 변경할 때 사용
        //저장되어있는 상태 호출


        var setting = findViewById<ImageView>(R.id.setting)
        var tutorial = findViewById<ImageView>(R.id.tuto)

        //기능 변경 *********통신으로 받기***********


        var userId = MySharedPreferences.getUserId(this)
        var retrofitClient = RetrofitClient.getInstance()
        var initMyApi = RetrofitClient.getRetrofitInterface()
        initMyApi.getParrotState(userId)
            .enqueue(object : Callback<ParrotState> {
                override fun onResponse(
                    call: Call<ParrotState>,
                    response: Response<ParrotState>,
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

        initMyApi.getPageSize(userId)
            .enqueue(object : Callback<PageSize> {
                override fun onResponse(
                    call: Call<PageSize>,
                    response: Response<PageSize>,
                ) {
                    // 통신으로 상태 받아오기
                    var body = response.body()
                    if(body!!.pageSize != "0")
                    {
                        pageSize = body!!.pageSize.toInt()
                        println("pagesize = " + pageSize)
                        println("PageSize = " + pageSize)
                        initMyApi.getParrotRecord(userId, pageSize.toString())
                            .enqueue(object : Callback<ParrotRecord> {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                                val time = System.currentTimeMillis()
                                var currentdate = dateFormat.format(Date(time)).toString()
                                override fun onResponse(
                                    call: Call<ParrotRecord>,
                                    response: Response<ParrotRecord>,
                                ) {
                                    // 통신으로 상태 받아오기
                                    var body = response.body()
                                    println("body this -> " + body.toString())
                                    try{
                                        if(currentdate != body!!.date){
                                            pageSize += 1
                                            date = currentdate
                                            feedCount = 0
                                            feedKind = 0
                                            playType = 0
                                            playResult = 0
                                            chatCount = 0
                                        }else{
                                            date = body!!.date
                                            feedCount = body!!.feedCount
                                            feedKind = body!!.feed
                                            playType = body!!.playType
                                            playResult = body!!.playResult
                                            chatCount = body!!.chatCount
                                        }
                                    } catch(e: NullPointerException){
                                        println("Catch!!")
                                    pageSize = 1
                                    date = currentdate
                                    pState = 0
                                    feedCount = 0
                                    feedKind = 0
                                    playType = 0
                                    playResult = 0
                                    chatCount = 0
                                    }

                                    saveParrotRecord()
                                }
                                override fun onFailure(call: Call<ParrotRecord>, t: Throwable) {
                                    println(t.message)
                                }
                            })

                    }
                    else{
                        println("else this")
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val time = System.currentTimeMillis()
                        var currentdate = dateFormat.format(Date(time)).toString()
                        pageSize = 1
                        date = currentdate
                        pState = 0
                        feedCount = 0
                        feedKind = 0
                        playType = 0
                        playResult = 0
                        chatCount = 0
                        var parrotRecord = ParrotRecord(pageSize.toString(),date,P_state,feedKind,feedCount,playType,playResult,chatCount)
                        saveParrotRecord()
                        initMyApi.sendParrotRecord(userId, pageSize.toString(), parrotRecord)
                            .enqueue(object : Callback<ParrotRecord>{
                                override fun onResponse(
                                    call: Call<ParrotRecord>,
                                    response: Response<ParrotRecord>
                                ) {
                                    var body = response.body()
                                    println(body)
                                }
                                override fun onFailure(call: Call<ParrotRecord>, t: Throwable) {
                                    println(t.message)
                                }
                            })
                    }
                }
                override fun onFailure(call: Call<PageSize>, t: Throwable) {
                    println(t.message)
                }

            })

        //***************************************
        val ani1 = findViewById<ImageView>(R.id.ani1)
        if(stress >= 50){
            Glide.with(this).load(R.raw.angry2).into(ani1)
            last_state = 3
        }else if(hunger >= 50){
            Glide.with(this).load(R.raw.angry1).into(ani1)
            last_state = 2
        }else if(boredom >= 50){
            Glide.with(this).load(R.raw.sleep).into(ani1)
            last_state = 1
        }else{
            Glide.with(this).load(R.raw.stand).into(ani1)
            last_state = 0
        }


        //흐름 계산
        timerTask = timer(period = 1000){
            counter++
            //reset()
            if(counter >= C_time){
                val ani1 = findViewById<ImageView>(R.id.ani1)
                hunger = calcul(hunger,1)
                boredom = calcul(boredom,1)
                stress = cal_stress(stress,hunger,boredom,1)
                counter = 0.0
                println("status = h," + hunger + ", " + boredom + ", " + stress)
                runOnUiThread{
                    changeanimation(ani1, boredom,hunger, stress)
                }

            }
            save()
        }
        if(check == "true"){
            supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, MemoryFragment())
                .commit()
            statusimg.setImageResource(R.drawable.underbar_dashboard)
            feedimg.setImageResource(R.drawable.underbar_forknife)
            brushimg.setImageResource(R.drawable.underbar_brush1)

        }else{
            changeFragment(2)
            statusimg.setImageResource(R.drawable.underbar_dashboard1)
            feedimg.setImageResource(R.drawable.underbar_forknife)
            brushimg.setImageResource(R.drawable.underbar_brush)
        }
        account.setOnClickListener({
            save()
            savedata()
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
        setting.setOnClickListener({
            val setintent = Intent(this, RecordActivity::class.java)
            startActivity(setintent)
        })
        tutorial.setOnClickListener({
            val tutointent = Intent(this, TutorialActivity::class.java)
            startActivity(tutointent)
        })
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
            MySharedPreferences.set_Stress(this,"0")
            MySharedPreferences.set_Boredom(this,"0")
            MySharedPreferences.set_Hunger(this,"0")
            MySharedPreferences.set_Counter(this,"0")
            MySharedPreferences.set_Affection(this,"0")
            MySharedPreferences.set_Level(this,"0")
            MySharedPreferences.set_food(this, "1")
            MySharedPreferences.set_finish(this, "false")
            MySharedPreferences.set_page(this, pageSize.toString())
            MySharedPreferences.set_date(this, date)
            MySharedPreferences.set_pState(this, P_state.toString())
            MySharedPreferences.set_feed(this, feedKind.toString())
            MySharedPreferences.set_feedCount(this, feedCount.toString())
            MySharedPreferences.set_playType(this, playType.toString())
            MySharedPreferences.set_playResult(this, playResult.toString())
            MySharedPreferences.set_chatCount(this, chatCount.toString())

            val tutointent = Intent(this, TutorialActivity::class.java)
            startActivity(tutointent)

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
        MySharedPreferences.set_Stress(this,stress.toString())
        MySharedPreferences.set_Boredom(this,boredom.toString())
        MySharedPreferences.set_Hunger(this,hunger.toString())
        MySharedPreferences.set_Counter(this,counter.toString())
        MySharedPreferences.set_Affection(this,affection.toString())
        MySharedPreferences.set_Level(this,level.toString())
        var current_time = System.currentTimeMillis()
        MySharedPreferences.set_Time(this, current_time)
        var userId = MySharedPreferences.getUserId(this)
        var parrotstate =
            ParrotState(hunger.toString(), stress.toString(), boredom.toString(), affection.toString(), level.toString(), counter.toString(), current_time)
        P_state = MySharedPreferences.get_pState(this).toInt()
        feedKind = MySharedPreferences.get_feed(this).toInt()
        feedCount = MySharedPreferences.get_feedCount(this).toInt()
        playType = MySharedPreferences.get_playType(this).toInt()
        playResult = MySharedPreferences.get_playResult(this).toInt()
        chatCount = MySharedPreferences.get_chatCount(this).toInt()
        var parrotRecord = ParrotRecord(pageSize.toString(),date,P_state,feedKind,feedCount,playType,playResult,chatCount)
        var retrofitClient = RetrofitClient.getInstance()
        var initMyApi = RetrofitClient.getRetrofitInterface()
        initMyApi.sendParrotState(userId, parrotstate)
            .enqueue(object : Callback<ParrotState> {
                override fun onResponse(
                    call: Call<ParrotState>,
                    response: Response<ParrotState>,
                ) {
                    var body = response.body()
                    println(body)
                }

                override fun onFailure(call: Call<ParrotState>, t: Throwable) {
                    println(t.message)
                }
            })
        initMyApi.sendParrotRecord(userId, pageSize.toString(), parrotRecord)
            .enqueue(object : Callback<ParrotRecord>{
                override fun onResponse(
                    call: Call<ParrotRecord>,
                    response: Response<ParrotRecord>
                ) {
                    var body = response.body()
                    println(body)
                }
                override fun onFailure(call: Call<ParrotRecord>, t: Throwable) {
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
    fun changeanimation(ani1 : ImageView, boredom : Int, hunger : Int, stress : Int){
        if(stress >= 50){
            P_state = 3
        }else if(hunger >= 50){
            P_state = 2
        }else if(boredom >= 50){
            P_state = 1
        }else{
            P_state = 0
        }
        if(P_state != last_state){
            if(stress >= 50){
                Glide.with(this).load(R.raw.angry2).into(ani1)
                last_state = 3
            }else if(hunger >= 50){
                Glide.with(this).load(R.raw.angry1).into(ani1)
                last_state = 2
            }else if(boredom >= 50){
                Glide.with(this).load(R.raw.sleep).into(ani1)
                last_state = 1
            }else{
                Glide.with(this).load(R.raw.stand).into(ani1)
                last_state = 0
            }
        }
        MySharedPreferences.set_pState(this,P_state.toString())
    }
    fun saveParrotRecord() {
        MySharedPreferences.set_page(this, pageSize.toString())
        MySharedPreferences.set_date(this, date)
        MySharedPreferences.set_pState(this, P_state.toString())
        MySharedPreferences.set_feed(this, feedKind.toString())
        MySharedPreferences.set_feedCount(this, feedCount.toString())
        MySharedPreferences.set_playType(this, playType.toString())
        MySharedPreferences.set_playResult(this, playResult.toString())
        MySharedPreferences.set_chatCount(this, chatCount.toString())
    }
}