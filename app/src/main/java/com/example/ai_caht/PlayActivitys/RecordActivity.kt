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
                page += 1
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
        var T_stateCount = ""
        var T_chatCount = ""
        var T_feedCount = ""
        var T_BFCount = ""
        var T_MKCount = ""
        var T_MRCount = ""
        if(stateCount.toInt() <= 0){
            T_stateCount = "오늘 앵무와 작별 인사를 할 때, 앵무는 행복해 보였다."    // 행복
        }else if(stateCount.toInt() == 1){
            T_stateCount = "앵무와 작별 인사를 할 때, 앵무는 어딘가 섭섭해 보였다."   // 심심함
        }else if(stateCount.toInt() == 2) {
            T_stateCount = "오늘의 앵무는 무언가 불만이 있어 보였다."                // 배고픔
        }else if(stateCount.toInt() >= 3) {
            T_stateCount = "앵무가 화났는지, 작별 인사를 받아주지 않았다."             // 스트레스
        }

        if(chatCount.toInt() <= 0){
            T_chatCount = "대화를 나누지 않았기 때문일지도 모른다."                  // 대화 0번
        }else if(chatCount.toInt() > 0 && chatCount.toInt() < 10){
            T_chatCount = "앵무가 느끼기에 대화는 조금 부족했을지도 모른다."             // 대화 1~ 9 사이
        }else if(chatCount.toInt() >= 10 && chatCount.toInt() < 20){
            T_chatCount = "앵무와 적당한 대화를 나눴다. 오늘의 대화는 만족스러운 듯 했다."    //대화 10 ~ 20 사이
        }else if(chatCount.toInt() >= 20){
            T_chatCount = "앵무와 많은 대화를 나눴다."                                 // 대화 20 이상
        }

        if(feedCount.toInt() == 0){
            T_feedCount = "생각해보니 오늘 앵무에게 밥을 주는 걸 깜빡했다."                 // 먹이 0 회
        }else if(feedCount.toInt() > 0 || feedCount.toInt() < 3){
            T_feedCount = "밥을 조금 적게 주었다. 앵무가 조금 배고플 수도 있었을 것 같다."    // 먹이 1~2회
        }else if(feedCount.toInt() >= 3){
            T_feedCount = "앵무가 배고픔을 느낄 수 없도록 철저하게 밥을 주었다."              // 먹이 3회 이상
        }

        if(BFCount.toInt() == 0){
            T_BFCount = "앵무가 오늘 맛있게 먹은 음식은 없다."                             // 맛있게 먹은 먹이 x
        }else{
            T_BFCount = "앵무는 오늘" + Want_food.want_food(BFCount.toInt()) +  "를 굉장히 맛있게 먹었다."     //맛있게 먹은 먹이가 있는 경우
        }

        if(MKCount.toInt() == 0){
            T_MKCount = "앵무와 놀아줄 시간이 없었다."                                  // 놀아주기 안했을 경우
        }else if(MKCount.toInt() == 1){
            T_MKCount = "앵무와 그림 맞추기 게임을 했다."                                // 놀아주기의 종류 나중에 맛있게 먹은 먹이와 동일하게 구현할 예정
        }

        if(MRCount.toInt() == 0){
            T_MKCount = "앵무가 놀아달라고 때를 썼지만 시간이 없었기 때문에 무시했다."                                  // 놀아주기 안했을 경우
        }else if(MKCount.toInt() == 1){
            T_MKCount = "앵무와 그림 맞추기 게임을 했지만, 실패했다."                                         // 놀아주기의 결과 실패
        }else if(MKCount.toInt() == 2){
            T_MKCount = "앵무와 그림 맞추기 게임을 했고, 앵무와 함께 성공했다."                                //  놀아주기의 결과 성공
        }

        month.setText(monthCount)
        day.setText(dayCount)
        state.setText(T_stateCount)
        chatcount.setText(T_chatCount)
        feedcount.setText(T_feedCount)
        bestfood.setText(T_BFCount)
        memorykind.setText(T_MKCount)
        memoryresult.setText(T_MRCount)
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