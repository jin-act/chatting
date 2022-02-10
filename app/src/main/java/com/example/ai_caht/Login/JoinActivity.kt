package com.example.ai_caht.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ai_caht.MainActivity
import com.example.ai_caht.R
import java.util.regex.Pattern


class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        val btn_SignUp = findViewById<Button>(R.id.btn_Join)
        val btn_ID = findViewById<Button>(R.id.btn_ID)
        val et_id = findViewById<EditText>(R.id.et_joinid)
        val et_pw = findViewById<EditText>(R.id.et_joinpw)
        val et_pwcheck = findViewById<EditText>(R.id.et_pwcheck)
        val btn_loginback = findViewById<TextView>(R.id.login)
        var checkPass = 0
        var checkId = 0

        btn_ID.setEnabled(false)
        et_pwcheck.setEnabled(false)
        et_pwcheck.setBackgroundResource(R.drawable.contents_box2)
        et_pw.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            //Enter key Action
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(this, "비밀번호를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
                checkPass = 1
                true
            } else false
        })
        et_id.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            //Enter key Action
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(this, "아이디를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
                checkId = 1
                true
            } else false
        })
        et_id.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btn_ID.setEnabled(true)
                btn_ID.setBackgroundResource(R.drawable.loginbutton)
                if(btn_ID.isEnabled){
                    checkId = 0
                }
            }

            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}
            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        })
        btn_ID.setOnClickListener {
            //**추가**
            // 웹 통신 아이디 중복 검사 및 저장
            checkId = 1
            Toast.makeText(this, "아이디를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
        et_pw.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", et_pw.text.toString())) {
                    et_pwcheck.setEnabled(true)
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box)
                    et_pw.setBackgroundResource(R.drawable.contents_box7)
                }
                else{
                    et_pwcheck.setEnabled(false)
                    et_pw.setBackgroundResource(R.drawable.contents_box6)
                }
                if(et_pwcheck.isEnabled){
                    checkPass = 0
                }
            }

            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}
            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        })
        et_pwcheck.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (Pattern.matches(et_pwcheck.text.toString(), et_pw.text.toString())) {
                    checkPass = 1
                }

            }

            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}
            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        })
        btn_loginback.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_SignUp.setOnClickListener {
            if(checkPass == 1 && checkId == 1)
            {
                MySharedPreferences.setUserId(this, et_id.text.toString())
                MySharedPreferences.setUserPass(this, et_pw.text.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if(checkId == 0){
                Toast.makeText(this, "아이디를 확인하세요", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}