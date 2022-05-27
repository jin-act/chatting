package com.example.ai_caht.PlayActivitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ai_caht.R
import org.w3c.dom.Text

class RecordActivity : AppCompatActivity() {
    var page = ""
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
        var dateresult = date.split("-")
        page = MySharedPreferences.get_page(this)
        date = MySharedPreferences.get_date(this)
        if(date != "0000-00-00"){
            dateresult = date.split("-")
        }
        stateCount = MySharedPreferences.get_pState(this)
        chatCount = MySharedPreferences.get_chatCount(this)
        feedCount = MySharedPreferences.get_feedCount(this)
        BFCount = MySharedPreferences.get_feed(this)
        MKCount = MySharedPreferences.get_playType(this)
        MRCount = MySharedPreferences.get_playResult(this)
        monthCount = dateresult.get(1)
        dayCount = dateresult.get(2)
        month.setText(monthCount)
        day.setText(dayCount)
        state.setText(stateCount)
        chatcount.setText(chatCount)
        feedcount.setText(feedCount)
        bestfood.setText(BFCount)
        memorykind.setText(MKCount)
        memoryresult.setText(MRCount)

    }
}