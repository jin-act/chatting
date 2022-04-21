package com.example.ai_caht.PlayActivitys

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class ChatActivity : AppCompatActivity() {
    private var recyclerView : RecyclerView? = null
    val chatList = ArrayList<ChatLayout>()
    private var mBinding:ActivityChatEditBinding? = null
    private val binding get() = mBinding!!
    val helper = DBHelper(this, "db_phone", null, 1)
    val adapter = RecycleAdapter(this)
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_edit)
        mBinding = ActivityChatEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var btn_send = findViewById<Button>(R.id.sendchatbtn)
        var whole_layout = findViewById<ConstraintLayout>(R.id.whole_layout)
        var btn_remove = findViewById<Button>(R.id.remove_btn)
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

        btn_send.setOnClickListener ({
            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("a hh:mm")
            val curTime = dateFormat.format(Date(time)).toString()
            var ChatEdit = findViewById<EditText>(R.id.inputchat)
            val chat: String = ChatEdit?.getText().toString()
            val chat_db = ChatLayout(null, R.drawable.ballon2, chat, Gravity.END, curTime, View.INVISIBLE, R.drawable.contents_box10, View.INVISIBLE)
            helper.insert_db(chat_db)
            adapter.comments.clear()
            adapter.comments.addAll(helper.select_db())
            ChatResponse()
            ChatEdit.setText(null)
            HideKeyBoard()
        })

        btn_remove.setOnClickListener({
            var list = adapter.list
            val data = list.iterator()
            val dialog = DeleteDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object: DeleteDialog.OnDialogClickListener{
                override fun onClicked(text: String){
                    if(text == "delete"){
                        if(!list.isEmpty()){
                            while(data.hasNext()){
                                val str = data.next()
                                helper.delete_db(adapter.comments.get(str))
                                adapter.comments.remove(adapter.comments.get(str))
                                adapter.notifyDataSetChanged()
                            }
                            for(i :Int in 0 until adapter.itemCount){
                                helper.update_db2(adapter.comments.get(i))
                                adapter.comments.clear()
                                adapter.comments.addAll(helper.select_db())
                                adapter.notifyDataSetChanged()
                            }
                            list.clear()
                        }
                    }
                    else{
                        Toast.makeText(context, "취소됨", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        })

    }
    fun Initaialize(){
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
        with(chatList){
            chatList.add(ChatLayout(null, R.drawable.ballon2,"반가워요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5,View.GONE))
        }
    }
    fun RecyclerViewSet(){
        adapter.helper = helper
        adapter.comments = chatList
        binding.recycleviewId.adapter = adapter
        binding.recycleviewId.layoutManager = LinearLayoutManager(this)
        //binding.recycleviewId.scrollToPosition(adapter.itemCount)
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
        val chatRequest = ChatRequest(chat)
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
                        val chat_db = ChatLayout(null, R.drawable.ballon2, "못 알아들었어요", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE)
                        helper.insert_db(chat_db)
                    }
                    else {
                        val chat_db = ChatLayout(null, R.drawable.ballon2, aichat, Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5, View.GONE)
                        helper.insert_db(chat_db)
                    }
                    adapter.comments.clear()
                    adapter.comments.addAll(helper.select_db())
                    //chatList.add(ChatLayout(null, R.drawable.ballon2, aichat, Gravity.START, curTime, View.VISIBLE))
                    //BotChat.setText(aichat)
                    RecyclerViewSet()
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


