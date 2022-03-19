package com.example.ai_caht.PlayActivitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ai_caht.R
import com.example.ai_caht.databinding.ActivityChatBinding
import com.example.ai_caht.test.BackPressCloseHandler
import com.example.ai_caht.test.Chat.ChatRequest
import com.example.ai_caht.test.Chat.ChatResponse
import com.example.ai_caht.test.Login.LoginResponse
import com.example.ai_caht.test.RetrofitClient
import com.example.ai_caht.test.initMyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {
    private var current_user : String? = null
    private var recyclerView : RecyclerView? = null
    val chatList = ArrayList<ChatLayout>()
    private lateinit var binding:ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_edit)

        var btn_send = findViewById<Button>(R.id.sendchatbtn)
        //var UserChat = findViewById<TextView>(R.id.userchat)
        var ChatEdit = findViewById<EditText>(R.id.inputchat)

        btn_send.setOnClickListener ({
            val chat: String = ChatEdit?.getText().toString()
            //로그인 통신
            //UserChat.setText(chat)
            current_user = "me"
            chatList.add(ChatLayout(current_user!!, chat))
            ChatResponse()
            RecyclerViewSet()
        })
    }

    fun RecyclerViewSet(){
        val adapter = RecyclerViewAdapter()
        adapter.comments = chatList
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter
    }

    fun ChatResponse() {
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
                    var aichat = body!!.userchat
                    aichat = aichat.replace("answer :","")
                    chatList.add(ChatLayout(current_user!!, aichat))
                    //BotChat.setText(aichat)
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
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ChatViewHolder>(){
        var comments = ArrayList<ChatLayout>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ChatViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
            return ChatViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerViewAdapter.ChatViewHolder, position: Int) {
            holder.message.text = comments[position].contents
            if(comments[position].user.equals("me")){
                holder.layout_main.gravity = Gravity.RIGHT
                holder.user.visibility = View.INVISIBLE
                holder.image.setImageResource(R.drawable.user)
            }
            else{
                holder.layout_main.gravity = Gravity.LEFT
                holder.user.visibility = View.VISIBLE
                holder.image.setImageResource(R.drawable.ballon2)
            }
        }

        override fun getItemCount(): Int {
            return comments.size
        }

        inner class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
            val user: TextView = view.findViewById(R.id.name)
            val message: TextView = view.findViewById(R.id.bot_chat)
            val image: ImageView = view.findViewById(R.id.chat_Image)
            val layout_destination: LinearLayout = view.findViewById(R.id.chat_destination)
            val layout_main: LinearLayout = view.findViewById(R.id.layout_main)
        }

    }
}

