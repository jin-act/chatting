package com.example.ai_caht.PlayActivitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.ai_caht.R
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var btn_send = findViewById<Button>(R.id.sendchatbtn)
        var UserChat = findViewById<TextView>(R.id.userchat)
        var ChatEdit = findViewById<EditText>(R.id.inputchat)


        btn_send.setOnClickListener ({
            val chat: String = ChatEdit?.getText().toString()
            //로그인 통신
            UserChat.setText(chat)
            ChatResponse()

        })
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
                    BotChat.setText(aichat)
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