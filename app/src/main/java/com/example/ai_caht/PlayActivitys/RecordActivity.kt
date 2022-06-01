package com.example.ai_caht.PlayActivitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.ai_caht.R
import com.example.ai_caht.test.Record.ParrotRecord
import com.example.ai_caht.test.RetrofitClient
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordActivity : AppCompatActivity() {
    var Maxpage = ""
    var page = 0
    var date = "0000-00-00"
    var monthCount = "00"
    var dayCount = "00"
    var stateCount = "0"
    var chatCount = "0"
    var feedCount = "0"
    var BFCount = "0"
    var MKCount = "0"
    var MRCount = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        var month = findViewById<TextView>(R.id.monthcount)
        var day = findViewById<TextView>(R.id.daycount)
        var state = findViewById<TextView>(R.id.pstatecoment)
        var chatcount = findViewById<TextView>(R.id.chatcount)
        var feedcount = findViewById<TextView>(R.id.feedcomment)
        var bestfood = findViewById<TextView>(R.id.bestfood)
        var memorykind = findViewById<TextView>(R.id.memorycomment)
        var memoryresult = findViewById<TextView>(R.id.resultcomment)
        var Next = findViewById<ImageView>(R.id.Next_BTN)
        var PRV = findViewById<ImageView>(R.id.PRV_BTN)
        var dateresult = date.split("-")
        Maxpage = MySharedPreferences.get_page(this)
        page = Maxpage.toInt()
        today()
        setText(month, day, state, chatcount, feedcount, bestfood, memorykind, memoryresult)


        var userId = MySharedPreferences.getUserId(this)
        var retrofitClient = RetrofitClient.getInstance()
        var initMyApi = RetrofitClient.getRetrofitInterface()

        //다음 버튼 최종 페이지와 비교, 현재 페이지가 최종 이상이라면, 최종 페이지로 변경
        Next.setOnClickListener {
            //값 계산
            println("next!")
            if(page < Maxpage.toInt() && page != (Maxpage.toInt()-1)){
                page += 1
                var parrotRecord = ParrotRecord(page.toString(),date,stateCount.toInt(),BFCount.toInt(),feedCount.toInt(),MKCount.toInt(),MRCount.toInt(),chatCount.toInt())
                initMyApi.getParrotRecord(userId,page.toString())
                    .enqueue(object : Callback<ParrotRecord> {
                        override fun onResponse(
                            call: Call<ParrotRecord>,
                            response: Response<ParrotRecord>
                        ) {
                            var body = response.body()
                            println(body)
                            
                            date = body!!.date
                            stateCount = body!!.parrotState.toString()
                            BFCount = body!!.feed.toString()
                            feedCount = body!!.feedCount.toString()
                            MKCount = body!!.playType.toString()
                            MRCount = body!!.playResult.toString()
                            chatCount = body!!.chatCount.toString()
                            dateresult = date.split("-")
                            monthCount = dateresult.get(1)
                            dayCount = dateresult.get(2)
                            setText(month, day, state, chatcount, feedcount, bestfood, memorykind, memoryresult)

                        }
                        override fun onFailure(call: Call<ParrotRecord>, t: Throwable) {
                            println(t.message)
                        }
                    })

            }else if(page == (Maxpage.toInt()-1)){
                today()
                setText(month, day, state, chatcount, feedcount, bestfood, memorykind, memoryresult)
            }
            else{
                // 값이 최대라면... 현재 동작 없음
            }
        }

        //이전 버튼 값 1과 비교, 현재 페이지가 1 이하라면, 1로 변경
        PRV.setOnClickListener {
            println("previous!")
            if(page > 1){
                page -= 1
                var parrotRecord = ParrotRecord(page.toString(),date,stateCount.toInt(),BFCount.toInt(),feedCount.toInt(),MKCount.toInt(),MRCount.toInt(),chatCount.toInt())
                initMyApi.getParrotRecord(userId,page.toString())
                    .enqueue(object : Callback<ParrotRecord> {
                        override fun onResponse(
                            call: Call<ParrotRecord>,
                            response: Response<ParrotRecord>
                        ) {
                            var body = response.body()
                            println(body)

                            date = body!!.date
                            stateCount = body!!.parrotState.toString()
                            BFCount = body!!.feed.toString()
                            feedCount = body!!.feedCount.toString()
                            MKCount = body!!.playType.toString()
                            MRCount = body!!.playResult.toString()
                            chatCount = body!!.chatCount.toString()
                            dateresult = date.split("-")
                            monthCount = dateresult.get(1)
                            dayCount = dateresult.get(2)
                            setText(month, day, state, chatcount, feedcount, bestfood, memorykind, memoryresult)

                        }
                        override fun onFailure(call: Call<ParrotRecord>, t: Throwable) {
                            println(t.message)
                        }
                    })

            }else{
                // 값이 1이라면... 현재 동작 없음
            }
        }
    }
    fun setText(month : TextView, day : TextView, state : TextView, chatcount : TextView, feedcount : TextView, bestfood : TextView, memorykind : TextView, memoryresult : TextView){
        month.setText(monthCount)
        day.setText(dayCount)
        state.setText(stateCount)
        chatcount.setText(chatCount)
        feedcount.setText(feedCount)
        bestfood.setText(BFCount)
        memorykind.setText(MKCount)
        memoryresult.setText(MRCount)
    }
    fun today(){
        date = MySharedPreferences.get_date(this)
        var dateresult = date.split("-")
        if(date != "0000-00-00"){
            dateresult = date.split("-")
        }
        //처음 화면, 최종 페이지
        stateCount = MySharedPreferences.get_pState(this)
        chatCount = MySharedPreferences.get_chatCount(this)
        feedCount = MySharedPreferences.get_feedCount(this)
        BFCount = MySharedPreferences.get_feed(this)
        MKCount = MySharedPreferences.get_playType(this)
        MRCount = MySharedPreferences.get_playResult(this)
        monthCount = dateresult.get(1)
        dayCount = dateresult.get(2)
    }
}