package com.example.ai_caht.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.ai_caht.MainActivity
import com.example.ai_caht.R
import java.util.regex.Pattern


class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        val btn_SignUp = findViewById<Button>(R.id.btn_Join)        // 회원가입 버튼
        val btn_ID = findViewById<Button>(R.id.btn_ID)              // ID확인 버튼
        val et_id = findViewById<EditText>(R.id.et_joinid)          // ID에디트 텍스트
        val et_pw = findViewById<EditText>(R.id.et_joinpw)          // pw에디트 텍스트
        val et_pwcheck = findViewById<EditText>(R.id.et_pwcheck)    // pw확인 에디트 텍스트
        val btn_loginback = findViewById<TextView>(R.id.login)      // 로그인 화면으로 다시 돌아가는 버튼
        var checkPass = 0                                           // 패스워드 유효성 검사
        var checkId = 0                                             // ID유효성 검사
        var num = 0                                                 // pw 확인 에디트 활성화 검사
        val dm = getResources().displayMetrics

        //시작 ID확인 버튼, pw확인 에디트 비활성화
        btn_ID.setEnabled(false)
        et_pwcheck.setEnabled(false)
        et_pwcheck.setBackgroundResource(R.drawable.contents_box2) // 회색 그림
        et_id.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btn_ID.setEnabled(true)
                btn_ID.setBackgroundResource(R.drawable.contents_box9)
                btn_ID.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.black))
                if(btn_ID.isEnabled){
                    checkId = 0
                }
            }
            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}
            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                et_id.setBackgroundResource(R.drawable.edittext_background)
                et_pw.setBackgroundResource(R.drawable.contents_box)
                if(num == 1){
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box)
                }
            }
        })
        btn_ID.setOnClickListener {
            //**추가**
            // 웹 통신 아이디 중복 검사 및 저장
            et_id.setBackgroundResource(R.drawable.contents_box7)
            et_pw.setBackgroundResource(R.drawable.contents_box)
            if(num == 1){
                et_pwcheck.setBackgroundResource(R.drawable.contents_box)
            }
            checkId = 1
            Toast.makeText(this, "아이디를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
        et_pw.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // pw체크 초기화
                checkPass = 0
                et_pwcheck.setText("")
                //pw 유효성 검사 유효하면 pw 확인 에디트 텍스트를 활성화
                if (Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", et_pw.text.toString())) {
                    et_pwcheck.setEnabled(true)
                    num = 1
                    et_pw.setBackgroundResource(R.drawable.contents_box7)
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box)
                }
                else{
                    et_pwcheck.setEnabled(false)
                    num = 0
                    et_pw.setBackgroundResource(R.drawable.edittext_background)
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box2)
                }
            }
            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {
            }
            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                et_id.setBackgroundResource(R.drawable.contents_box)
                et_pw.setBackgroundResource(R.drawable.edittext_background)
            }
        })
        et_pwcheck.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (Pattern.matches(et_pwcheck.text.toString(), et_pw.text.toString())) {
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box7)
                    checkPass = 1
                }
                else{
                    checkPass = 0
                }

            }
            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}
            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                et_id.setBackgroundResource(R.drawable.contents_box)
                et_pw.setBackgroundResource(R.drawable.contents_box)
                et_pwcheck.setBackgroundResource(R.drawable.edittext_background)
            }
        })
        //로그인 화면으로 돌아가기
        btn_loginback.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        //회원가입 완료버튼 눌렀을 때
        btn_SignUp.setOnClickListener {
            //ID, pw 유효성이 모두 완료되었을 때, 회원가입 완료
            join(checkId,checkPass,num)
        }
    }
    fun join(checkId:Int, checkPass:Int, num:Int){
        val et_id = findViewById<EditText>(R.id.et_joinid)          // ID에디트 텍스트
        val et_pw = findViewById<EditText>(R.id.et_joinpw)          // pw에디트 텍스트
        val et_pwcheck = findViewById<EditText>(R.id.et_pwcheck)    // pw확인 에디트 텍스트
        val dm = getResources().displayMetrics
        if(checkPass == 1 && checkId == 1)
        {
            MySharedPreferences.setUserId(this, et_id.text.toString())
            MySharedPreferences.setUserPass(this, et_pw.text.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        //ID 유효성이 완료되지 않았을 때
        else if(checkId == 0){

            Toast.makeText(this, "아이디를 확인하세요", Toast.LENGTH_SHORT).show()
            et_id.setBackgroundResource(R.drawable.contents_box6)
            et_pw.setBackgroundResource(R.drawable.contents_box)
            if(num == 1)
            {
                et_pwcheck.setBackgroundResource(R.drawable.contents_box)
            }
        }
        else{
            Toast.makeText(this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            et_id.setBackgroundResource(R.drawable.contents_box)
            et_pw.setBackgroundResource(R.drawable.contents_box6)
            if(num == 1)
            {
                et_pwcheck.setBackgroundResource(R.drawable.contents_box)
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