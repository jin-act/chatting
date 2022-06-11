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
import com.example.ai_caht.test.Chat.*
import com.google.android.material.snackbar.Snackbar
import org.json.JSONTokener
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
    var food_num: String = "1"
    var food: String = ""
    val food_text: String = "뭐 먹을래?"
    var hunger: Int = 0
    var boredem: Int = 0
    var stress: Int = 0
    var condition: String = ""
    var mBackWait:Long = 0
    var chatCount :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_edit)
        mBinding = ActivityChatEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var btn_send = findViewById<Button>(R.id.sendchatbtn)
        var whole_layout = findViewById<ConstraintLayout>(R.id.whole_layout)
        var btn_remove = findViewById<Button>(R.id.remove_btn)
        var ChatEdit = findViewById<EditText>(R.id.inputchat)
        var deleteLayout = findViewById<LinearLayout>(R.id.delete_layout)

        //var UserChat = findViewById<TextView>(R.id.userchat)
        Initaialize()
        RecyclerViewSet()
        if(MySharedPreferences.get_chatCount(context) == "") {
            chatCount = 0
        }
        else{
            chatCount = MySharedPreferences.get_chatCount(context).toInt()
        }
        radiobtn_off()
        select_chat()
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
            status_message()
        }

        ChatEdit.setOnClickListener {
            adapter.rdoCheck = true
        }

        btn_remove.setOnClickListener {
            var list = adapter.list
            var data_list = adapter.data
            var id_list = adapter.id
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
                                adapter.comments.remove(adapter.comments.get(str))
                                adapter.notifyDataSetChanged()
                                Handler(Looper.getMainLooper()).postDelayed({
                                    select_chat()
                                }, 500)

                            }
                            for (i in 0 until list_size) {
                                val chatRequest = ChatRequest(data_list[i], userID)
                                val idRequest = id_list[i]
                                delete_chat(idRequest.toString())
                                Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show()
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
                            radiobtn_off()
                            adapter.notifyDataSetChanged()
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
            radiobtn_off()
            adapter.notifyDataSetChanged()
            adapter.rdoCheck = false
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

    fun status_message(){
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
        val parrotTalk = condition
        var ChatEdit = findViewById<EditText>(R.id.inputchat)
        var btn_send = findViewById<Button>(R.id.sendchatbtn)
        var current_condition = ""
        when(parrotTalk){
            "스트레스" -> {
                current_condition = "앵무는 스트레스가 쌓여 있어요"
            }
            "배고픔" ->{
                current_condition = "앵무는 배가 고파요"
            }
            "심심함" ->{
                current_condition = "앵무는 심심한듯 합니다"
            }
            else ->{
                current_condition = "앵무는 기분이 좋은듯 합니다"
            }
        }
        Toast.makeText(context, current_condition, Toast.LENGTH_SHORT).show()

        /*
        val status = ChatLayout(null, R.drawable.ballon2, current_condition, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
        insert_chat(status)
        Handler(Looper.getMainLooper()).postDelayed({
            select_chat()
        }, 500)
        */

        btn_send.setOnClickListener {
            val chat: String = ChatEdit?.getText().toString()
            val chat_me = ChatLayout(null, R.drawable.ballon2, chat, Gravity.END, curTime, View.INVISIBLE, R.drawable.contents_box10, View.INVISIBLE, Gravity.END)
            HideKeyBoard()
            ChatEdit.setText(null)
            insert_chat(chat_me)
            if(chat.equals(food_text)){
                val chat_food = ChatLayout(null, R.drawable.ballon2, "저는 " +food+ "가 좋아요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                Handler(Looper.getMainLooper()).postDelayed({
                    insert_chat(chat_food)
                }, 1000)
                Handler(Looper.getMainLooper()).postDelayed({
                    select_chat()
                }, 1500)
                adapter.notifyDataSetChanged()
            }
            else {
                when (parrotTalk) {
                    "스트레스" -> {
                        val chat_stress = ChatLayout(null, R.drawable.ballon2, "아무말도 하기 싫어요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                        Handler(Looper.getMainLooper()).postDelayed({
                            insert_chat(chat_stress)
                        }, 1000)
                        Handler(Looper.getMainLooper()).postDelayed({
                            select_chat()
                        }, 1500)
                        btn_send.setOnClickListener {
                            val chat2: String = ChatEdit?.getText().toString()
                            val chat_me2 = ChatLayout(null, R.drawable.ballon2, chat2, Gravity.END, curTime, View.INVISIBLE, R.drawable.contents_box10, View.INVISIBLE, Gravity.END)
                            HideKeyBoard()
                            ChatEdit.setText(null)
                            insert_chat(chat_me2)
                            if(chat2.equals(food_text)){
                                val chat_food = ChatLayout(null, R.drawable.ballon2, "저는 " +food+ "가 좋아요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    insert_chat(chat_food)
                                }, 1000)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    select_chat()
                                }, 1500)
                                adapter.notifyDataSetChanged()
                            }
                            else {
                                val chat_stress2 = ChatLayout(null, R.drawable.ballon2, ".....", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    insert_chat(chat_stress2)
                                }, 1000)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    select_chat()
                                }, 1500)
                                btn_send.setOnClickListener {
                                    Toast.makeText(context, "앵무의 스트레스 수치가 높아 대화하기를 싫어합니다", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@ChatActivity, PlayActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                    "배고픔" -> {
                        ChatResponse()
                    }
                    "심심함" -> {
                        ChatResponse()
                    }
                    else -> {
                        ChatResponse()
                    }
                }
            }
        }
    }

    fun delete_button_set(){
        var deleteLayout = findViewById<LinearLayout>(R.id.delete_layout)
        var chatLayout = findViewById<LinearLayout>(R.id.edittext)

        deleteLayout.visibility = View.VISIBLE
        chatLayout.visibility = View.INVISIBLE
    }

    fun Initaialize(){
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
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
                        insert_chat(chat_db)
                        Handler(Looper.getMainLooper()).postDelayed({
                            select_chat()
                        }, 500)
                    }
                    else {
                        val chat_db = ChatLayout(null, R.drawable.ballon2, aichat, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE, Gravity.START)
                        //helper.insert_db(chat_db)
                        insert_chat(chat_db)
                        Handler(Looper.getMainLooper()).postDelayed({
                            select_chat()
                        }, 500)
                    }
                    //adapter.comments.addAll(helper.select_db())
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

    fun select_chat(){
        var initMyApi = RetrofitClient.getRetrofitInterface()
        val userId = MySharedPreferences.getUserId(this)
        initMyApi.selectAll(userId)?.enqueue(object : Callback<List<AdapterResponse>> {
            override fun onResponse(call: Call<List<AdapterResponse>>, response: Response<List<AdapterResponse>>) {
                //통신 성공
                if (response.isSuccessful) {
                    val list = mutableListOf<ChatLayout>()
                    val body = response.body()
                    if (body != null) {
                        for(i in body) {
                            val id = i.id
                            val profile = i.profile
                            val contents = i.contents
                            val position = i.position
                            val time = i.time
                            val visibility = i.visibility
                            val textBox = i.textBox
                            val radio = i.radio
                            val timeText = i.timeText
                            list.add(ChatLayout(id, profile, contents, position, time, visibility, textBox, radio, timeText))
                            adapter.comments.clear()
                            adapter.comments.addAll(list)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<AdapterResponse>?>, t: Throwable) {
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

    fun insert_chat(chatLayout :ChatLayout){
        var initMyApi = RetrofitClient.getRetrofitInterface()
        val userId = MySharedPreferences.getUserId(this)
        val adapterRequest = AdapterRequest(chatLayout.profile, chatLayout.contents, chatLayout.position,
        chatLayout.time, chatLayout.visibility, chatLayout.textBox, chatLayout.radio, chatLayout.timeText)

        initMyApi.insertDB(userId, adapterRequest)?.enqueue(object : Callback<IdResponse> {
            override fun onResponse(call: Call<IdResponse>, response: Response<IdResponse>) {
                //통신 성공
                if (response.isSuccessful) {
                    val body = response.body()
                    val id = body!!.id
                }
            }

            override fun onFailure(call: Call<IdResponse?>, t: Throwable) {
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

    fun radiobtn_off(){
        var initMyApi = RetrofitClient.getRetrofitInterface()
        val userId = MySharedPreferences.getUserId(this)
        initMyApi.selectAll(userId)?.enqueue(object : Callback<List<AdapterResponse>> {
            override fun onResponse(call: Call<List<AdapterResponse>>, response: Response<List<AdapterResponse>>) {
                //통신 성공
                if (response.isSuccessful) {
                    val list = mutableListOf<ChatLayout>()
                    val body = response.body()
                    if (body != null) {
                        for(i in body) {
                            val id = i.id
                            val profile = i.profile
                            val contents = i.contents
                            val position = i.position
                            val time = i.time
                            val visibility = i.visibility
                            val textBox = i.textBox
                            val radio = View.GONE
                            val timeText = i.timeText
                            list.add(ChatLayout(id, profile, contents, position, time, visibility, textBox, radio, timeText))
                            adapter.comments.clear()
                            adapter.comments.addAll(list)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<AdapterResponse>?>, t: Throwable) {
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

    fun delete_chat(chatId :String){
        val userId = MySharedPreferences.getUserId(this)
        var initMyApi = RetrofitClient.getRetrofitInterface()

        initMyApi.deleteDB(userId, chatId)?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                //통신 성공
                if (response.isSuccessful) {
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
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


