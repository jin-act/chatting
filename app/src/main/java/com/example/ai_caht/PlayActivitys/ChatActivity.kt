package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ai_caht.R
import com.example.ai_caht.databinding.ActivityChatEditBinding
import com.example.ai_caht.test.Chat.ChatRequest
import com.example.ai_caht.test.Chat.ChatResponse
import com.example.ai_caht.test.Login.LoginResponse
import com.example.ai_caht.test.RetrofitClient
import com.example.ai_caht.test.initMyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.example.ai_caht.*
import com.example.ai_caht.test.Chat.ParrotTalkRequest
import com.example.ai_caht.test.Chat.ParrotTalkResponse
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.timer

class ChatActivity : AppCompatActivity() {
    private var recyclerView : RecyclerView? = null
    val chatList = ArrayList<ChatLayout>()
    private var mBinding:ActivityChatEditBinding? = null
    private val binding get() = mBinding!!
    val helper = DBHelper(this, "DATABASE_CHAT", null, 1)
    val adapter = RecycleAdapter(this)
    private val context = this
    var userID: String = ""
    var food_num: String = ""
    var food: String = ""
    val food_text: String = "뭐 먹을래?"
    var hunger: Int = 0
    var boredem: Int = 0
    var stress: Int = 0
    var condition: String = ""
    var mBackWait:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_edit)
        mBinding = ActivityChatEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var btn_send = findViewById<Button>(R.id.sendchatbtn)
        var whole_layout = findViewById<ConstraintLayout>(R.id.whole_layout)
        var btn_remove = findViewById<Button>(R.id.remove_btn)
        var ChatEdit = findViewById<EditText>(R.id.inputchat)
        //var UserChat = findViewById<TextView>(R.id.userchat)
        Initaialize()
        RecyclerViewSet()
        adapter.comments.clear()
        for(i :Int in 0 until adapter.itemCount) {
            helper.update_db2(adapter.comments.get(i))
        }
        adapter.comments.addAll(helper.select_db())
        whole_layout.setOnClickListener {
            HideKeyBoard()
        }
        val check = MySharedPreferences.get_check(context)
        userID = MySharedPreferences.getUserId(context)
        food_num = MySharedPreferences.get_food(context)
        food = Want_food.want_food(food_num.toInt())
        hunger = MySharedPreferences.get_Hunger(context).toInt()
        boredem = MySharedPreferences.ger_Boredom(context).toInt()
        stress = MySharedPreferences.get_Stress(context).toInt()
        // Toast.makeText(context, hunger, Toast.LENGTH_SHORT).show()
        if(stress >= 50){
            condition = "스트레스"
        }
        else if(stress < 50 && hunger >= 50 && boredem < 50){
            condition = "배고픔"
        }
        else if(stress < 50 && hunger < 50 && boredem >= 50){
            condition = "심심함"
        }
        else if(stress < 50 && hunger >= 50 && boredem >= 50){
            condition = "스트레스"
        }
        else{
            condition = "보통"
        }

        if(check != "true"){
            start_message()
        }
        /*
        val handlerTask = object : Runnable{
            override fun run() {
                val time = System.currentTimeMillis()
                val dateFormat = SimpleDateFormat("a hh:mm")
                val curTime = dateFormat.format(Date(time)).toString()
                val parrotTalk = ParrotTalkRequest(condition)
                var initMyApi = RetrofitClient.getRetrofitInterface()
                //Toast.makeText(context, "15초 경과", Toast.LENGTH_SHORT).show()
                initMyApi.parrotTalk(parrotTalk)?.enqueue(object : Callback<ParrotTalkResponse> {
                    override fun onResponse(call: Call<ParrotTalkResponse>, response: Response<ParrotTalkResponse>) {
                        //통신 성공
                        if(response.isSuccessful) {
                            val body = response.body()
                            val ai_question = body!!.question
                            val ai_answer = body.answer
                            val chat_db = ChatLayout(null, R.drawable.ballon2, ai_question, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                            helper.insert_db(chat_db)
                            adapter.comments.clear()
                            adapter.comments.addAll(helper.select_db())
                            RecyclerViewSet()

                            btn_send.setOnClickListener {
                                cond = true
                                val chat: String = ChatEdit?.getText().toString()
                                val chat_me = ChatLayout(null, R.drawable.ballon2, chat, Gravity.END, curTime, View.INVISIBLE, R.drawable.contents_box10, View.INVISIBLE, Gravity.END)
                                helper.insert_db(chat_me)
                                adapter.comments.clear()
                                adapter.comments.addAll(helper.select_db())
                                val chat_db2 = ChatLayout(null, R.drawable.ballon2, ai_answer, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                                helper.insert_db(chat_db2)
                                adapter.comments.clear()
                                adapter.comments.addAll(helper.select_db())
                                RecyclerViewSet()
                                ChatEdit.setText(null)
                                HideKeyBoard()
                                finish()
                                startActivity(intent)
                            }
                        }
                    }

                        override fun onFailure(call: Call<ParrotTalkResponse?>, t: Throwable) {
                            val builder = AlertDialog.Builder(this@ChatActivity)
                            println(t.message)
                            builder.setTitle("알림")
                                    .setMessage("예상치 못한 오류입니다.\n 고객센터에 문의바랍니다.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show()
                        }
                    })
            }
        }

        if(!adapter.rdoCheck) {
            //Toast.makeText(context, adapter.rdoCheck.toString(), Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed(handlerTask, 60000)
        }

        if(adapter.rdoCheck){
            //Toast.makeText(context, adapter.rdoCheck.toString(), Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).removeCallbacksAndMessages(handlerTask)
        } */

        btn_send.setOnClickListener {
            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("a hh:mm")
            val curTime = dateFormat.format(Date(time)).toString()
            var ChatEdit = findViewById<EditText>(R.id.inputchat)
            val chat: String = ChatEdit?.getText().toString()
            val chat_db = ChatLayout(null, R.drawable.ballon2, chat, Gravity.END, curTime, View.INVISIBLE, R.drawable.contents_box10, View.INVISIBLE, Gravity.END)
            helper.insert_db(chat_db)
            if(chat.equals(food_text)){
                val chat_db = ChatLayout(null, R.drawable.ballon2, "저는 " +food+ "가 좋아요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                helper.insert_db(chat_db)
                adapter.comments.clear()
                adapter.comments.addAll(helper.select_db())
                adapter.notifyDataSetChanged()
            }
            else {
                ChatResponse()
            }
            ChatEdit.setText(null)
            HideKeyBoard()
        }

        ChatEdit.setOnClickListener {
            adapter.rdoCheck = true
        }

        btn_remove.setOnClickListener {
            var list = adapter.list
            var data_list = adapter.data
            val list_size = list.count()
            val data = list.iterator()
            val dialog = DeleteDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : DeleteDialog.OnDialogClickListener {
                override fun onClicked(text: String) {
                    if (text == "delete") {
                        if (list.isNotEmpty()) {
                            while (data.hasNext()) {
                                val str = data.next()
                                helper.delete_db(adapter.comments.get(str))
                                adapter.comments.remove(adapter.comments.get(str))
                                adapter.notifyDataSetChanged()
                            }
                            for (i in 0 until list_size) {
                                val chatRequest = ChatRequest(data_list[i], userID)
                                Toast.makeText(context, data_list.toString(), Toast.LENGTH_SHORT).show()
                                var initMyApi = RetrofitClient.getRetrofitInterface()
                                initMyApi.deleteChat(chatRequest)?.enqueue(object : Callback<ChatResponse> {
                                    override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                                        //통신 성공
                                        if (response.isSuccessful) {
                                        }
                                    }

                                    override fun onFailure(call: Call<ChatResponse?>, t: Throwable) {
                                        val builder = AlertDialog.Builder(this@ChatActivity)
                                        println(t.message)
                                        builder.setTitle("알림")
                                                .setMessage("예상치 못한 오류입니다.\n 고객센터에 문의바랍니다.")
                                                .setPositiveButton("확인", null)
                                                .create()
                                                .show()
                                    }
                                })
                            }
                            for (i: Int in 0 until adapter.itemCount) {
                                helper.update_db2(adapter.comments.get(i))
                                adapter.comments.clear()
                                adapter.comments.addAll(helper.select_db())
                                adapter.notifyDataSetChanged()
                            }
                            list.clear()
                            data_list.clear()
                            adapter.rdoCheck = false
                        }
                    } else {
                        Toast.makeText(context, "취소됨", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        if(adapter.rdoCheck){
            for (i: Int in 0 until adapter.itemCount) {
                adapter.rdoCheck = false
                helper.update_db2(adapter.comments.get(i))
                adapter.comments.clear()
                adapter.comments.addAll(helper.select_db())
                adapter.notifyDataSetChanged()
            }
        }

        if(!adapter.rdoCheck && System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        }
        else{
            if(!adapter.rdoCheck) {
                MySharedPreferences.clear_check(context, "false")
                super.onBackPressed()
            }
        }
    }

    override fun onStop() {
        adapter.rdoCheck = true
        super.onStop()
    }

    fun start_message() {
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
        val parrotTalk = ParrotTalkRequest(condition)
        var ChatEdit = findViewById<EditText>(R.id.inputchat)
        var btn_send = findViewById<Button>(R.id.sendchatbtn)
        var initMyApi = RetrofitClient.getRetrofitInterface()
        initMyApi.parrotTalk(parrotTalk)?.enqueue(object : Callback<ParrotTalkResponse> {
            override fun onResponse(call: Call<ParrotTalkResponse>, response: Response<ParrotTalkResponse>) {
                //통신 성공
                if(response.isSuccessful) {
                    val body = response.body()
                    val ai_question = body!!.question
                    val ai_answer = body.answer
                    val chat_db = ChatLayout(null, R.drawable.ballon2, ai_question, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                    helper.insert_db(chat_db)
                    adapter.comments.clear()
                    adapter.comments.addAll(helper.select_db())
                    RecyclerViewSet()

                    btn_send.setOnClickListener {
                        val chat: String = ChatEdit?.getText().toString()
                        val chat_me = ChatLayout(null, R.drawable.ballon2, chat, Gravity.END, curTime, View.INVISIBLE, R.drawable.contents_box10, View.INVISIBLE, Gravity.END)
                        helper.insert_db(chat_me)
                        adapter.comments.clear()
                        adapter.comments.addAll(helper.select_db())
                        val chat_db2 = ChatLayout(null, R.drawable.ballon2, ai_answer, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                        helper.insert_db(chat_db2)
                        adapter.comments.clear()
                        adapter.comments.addAll(helper.select_db())
                        RecyclerViewSet()
                        ChatEdit.setText(null)
                        HideKeyBoard()
                        MySharedPreferences.set_check(context, "true")
                        finish()
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<ParrotTalkResponse?>, t: Throwable) {
                val builder = AlertDialog.Builder(this@ChatActivity)
                println(t.message)
                builder.setTitle("알림")
                        .setMessage("예상치 못한 오류입니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show()
            }
        })
    }

    fun Initaialize(){
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
        with(chatList){
            chatList.add(ChatLayout(null, R.drawable.ballon2,"반가워요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5,View.GONE, Gravity.START))
        }
    }

    fun RecyclerViewSet(){
        adapter.helper = helper
        adapter.comments = chatList
        binding.recycleviewId.adapter = adapter
        binding.recycleviewId.layoutManager = LinearLayoutManager(this)
        with(binding) {
            recycleviewId.scrollToPosition(adapter.itemCount -1)
        }
    }

    fun HideKeyBoard(){
        var view = this.currentFocus
        if(view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun ChatResponse(){
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
        var ChatEdit = findViewById<EditText>(R.id.inputchat)
        val chat: String = ChatEdit.getText().toString().trim { it <= ' ' }
        //String userPassword = passwordText.getText().toString().trim();
        val chatRequest = ChatRequest(chat, userID)
        //retrofit 생성
        var retrofitClient = RetrofitClient.getInstance()
        var initMyApi = RetrofitClient.getRetrofitInterface()
        var BotChat = findViewById<TextView>(R.id.botchat)
        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        initMyApi.getChatResponse(chatRequest)?.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(
                call: Call<ChatResponse>,
                response: Response<ChatResponse>) {
                //통신 성공
                if (response.isSuccessful) {
                    val body = response.body()
                    val aichat = body!!.userchat
                    //aichat = aichat.replace("answer :","")
                    if (aichat == null){
                        val chat_db = ChatLayout(null, R.drawable.ballon2, "못 알아들었어요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                        helper.insert_db(chat_db)
                    }
                    else {
                        val chat_db = ChatLayout(null, R.drawable.ballon2, aichat, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                        helper.insert_db(chat_db)
                    }
                    adapter.comments.clear()
                    adapter.comments.addAll(helper.select_db())
                    //chatList.add(ChatLayout(null, R.drawable.ballon2, aichat, Gravity.START, curTime, View.VISIBLE))
                    //BotChat.setText(aichat)
                    RecyclerViewSet()
                    adapter.rdoCheck = true
                }
            }

            //통신 실패
            override fun onFailure(call: Call<ChatResponse?>, t: Throwable) {
                val builder = AlertDialog.Builder(this@ChatActivity)
                println(t.message)
                builder.setTitle("알림")
                    .setMessage("예상치 못한 오류입니다.\n 고객센터에 문의바랍니다.")
                    .setPositiveButton("확인", null)
                    .create()
                    .show()
            }
        })
    }

}


