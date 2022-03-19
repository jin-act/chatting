package com.example.ai_caht.PlayActivitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.ai_caht.MainActivity
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //버튼과 텍트스뷰 생성 btn mainscreen을 activity_main의 버튼과 텍스트뷰와 연동
        val btn_mainscreen = findViewById<ImageView>(R.id.start)
        val tv_info = findViewById<TextView>(R.id.tv_info)
        val btn_logout = findViewById<Button>(R.id.btn_logout)
        Toast.makeText(
            this,
            "${MySharedPreferences.getUserId(this)}님 로그인 되었습니다.",
            Toast.LENGTH_SHORT
        ).show()
        tv_info.setText("환영합니다 " + MySharedPreferences.getUserId(this) + " 님!")
        btn_logout.setOnClickListener {
            //임시 저장 데이터 삭제
            //MySharedPreferences.clearUser(this)
            //자동 로그인 확인
            MySharedPreferences.autochecked(this, "0")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //btn_mainscreen작동
        btn_mainscreen.setOnClickListener({
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        })
    }
}