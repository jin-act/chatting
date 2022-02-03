package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Pattern


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val btn_SignUp = findViewById<Button>(R.id.btn_sign)
        val ed_ID = findViewById<EditText>(R.id.Get_Id)
        val ed_Pass = findViewById<EditText>(R.id.Get_Pass)
        val btn_ID = findViewById<Button>(R.id.ID_check)
        val btn_Pass = findViewById<Button>(R.id.Pass_check)
        var checkPass = 0
        var checkId = 0

        btn_ID.setEnabled(false)
        btn_Pass.setEnabled(false)
        ed_Pass.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            //Enter key Action
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(this, "비밀번호를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
                checkPass = 1
                true
            } else false
        })
        ed_ID.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            //Enter key Action
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(this, "아이디를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
                checkId = 1
                true
            } else false
        })
        ed_ID.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btn_ID.setEnabled(true)
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
        ed_Pass.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", ed_Pass.text.toString())) {
                    btn_Pass.setEnabled(true)
                }
                else{
                    btn_Pass.setEnabled(false)
                }
                if(btn_Pass.isEnabled){
                    checkPass = 0
                }
            }
            
            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}
            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        })
        btn_Pass.setOnClickListener {
            //**추가**
            // 웹 통신 비번 저장
            Toast.makeText(this, "비밀번호를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
            checkPass = 1
        }
        btn_SignUp.setOnClickListener {
            if(checkPass == 1 && checkId == 1)
            {
                MySharedPreferences.setUserId(this, ed_ID.text.toString())
                MySharedPreferences.setUserPass(this, ed_Pass.text.toString())
                val intent = Intent(this, LoginActivity::class.java)
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