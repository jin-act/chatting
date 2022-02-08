package com.example.ai_caht.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ai_caht.MainActivity
import com.example.ai_caht.R
import java.util.*
import kotlin.concurrent.timer

class LoginActivity : AppCompatActivity() {
    var check = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // SharedPreferences 안에 값이 저장되어 있지 않을 때 -> Login
        Login()
    }

    fun Login(){
        val btn_login = findViewById<Button>(R.id.btn_login)
        val et_id = findViewById<EditText>(R.id.et_id)
        val et_pw = findViewById<EditText>(R.id.et_pw)
        et_pw.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        //**추가 및 변경**
        //아이디 비번 유효성 검사
        //유효성 문제가 없다면 아이디 비번 웹 통신을 통해서 확인
        et_pw.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            //Enter key Action
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                LoginButton()
                true
            } else false
        })
        et_id.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            //Enter key Action
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                LoginButton()
                true
            } else false
        })
        btn_login.setOnClickListener {
            LoginButton()
        }
        val btn_signup = findViewById<TextView>(R.id.join)
        btn_signup.setOnClickListener {
            var intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun LoginButton() {
        val btn_login = findViewById<Button>(R.id.btn_login)
        val et_id = findViewById<EditText>(R.id.et_id)
        val et_pw = findViewById<EditText>(R.id.et_pw)
        val text_Timer = findViewById<TextView>(R.id.text_timer)
        if (et_id.text.toString() != MySharedPreferences.getUserId(this)) { // 아이디 데이터 확인
            Toast.makeText(this, "아이디를 확인하세요", Toast.LENGTH_SHORT).show()
            check += 1
        } else if (et_pw.text.toString() != MySharedPreferences.getUserPass(this)) { // 아이디 패스워드 매칭 확인
            Toast.makeText(this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            check += 1
        } else {
            MySharedPreferences.autochecked(this, "1")
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (check >= 5) {
            btn_login.setEnabled(false) //버튼 비활성화
            btn_login.setBackgroundResource(R.drawable.contents_box4)
            et_id.setEnabled(false)
            et_id.setBackgroundResource(R.drawable.edittext_background2)
            et_pw.setEnabled(false)
            et_pw.setBackgroundResource(R.drawable.edittext_background2)

            start()
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                btn_login.setEnabled(true) //버튼 활성화
                btn_login.setBackgroundResource(R.drawable.loginbutton)
                et_id.setEnabled(true)
                et_id.setBackgroundResource(R.drawable.edittext_background)
                et_pw.setEnabled(true)
                et_pw.setBackgroundResource(R.drawable.edittext_background)
                check = 0
                text_Timer.setText("")
            }, 5000)
        }

    }
    private fun start() {
        var timerTask: Timer? = null
        var time = 0
        val text_Timer = findViewById<TextView>(R.id.text_timer)
        timerTask = timer(period = 10) {	// timer() 호출
            time++	// period=10, 0.01초마다 time를 1씩 증가
            val sec = (time / 100) + 1	// time/100, 나눗셈의 몫 (초 부분)
            val Ltime = 5-sec
            if(sec >= 4){
                timerTask?.cancel()
            }
            // UI조작을 위한 메서드
            runOnUiThread {
                text_Timer.text = "$Ltime 초 후에 다시 시도해 주세요"	// TextView 세팅
            }
        }

    }
}