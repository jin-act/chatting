package com.example.ai_caht

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ai_caht.Login.LoginActivity
import com.example.ai_caht.Login.MySharedPreferences


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //버튼과 텍트스뷰 생성 btn mainscreen을 activity_main의 버튼과 텍스트뷰와 연동
        val btn_mainscreen = findViewById<ImageView>(R.id.start)
        val tv_info = findViewById<TextView>(R.id.tv_info)
        val btn_logout = findViewById<Button>(R.id.btn_logout)
        firsttime()
        if(MySharedPreferences.getautochecked(this) == "0") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else { // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity로 이동
            Toast.makeText(this, "${MySharedPreferences.getUserId(this)}님 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            tv_info.setText("환영합니다 " + MySharedPreferences.getUserId(this) + " 님!")
        }

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
    fun firsttime(){
        val pref = getSharedPreferences("isFirst", MODE_PRIVATE)
        val first = pref.getBoolean("isFirst", false)
        if (first == false) {
            Log.d("Is first Time?", "first")
            val editor = pref.edit()
            editor.putBoolean("isFirst", true)
            editor.commit()
            MySharedPreferences.autochecked(this, "0")
            //앱 최초 실행시 하고 싶은 작업
        } else {
            Log.d("Is first Time?", "not first")
        }
    }
}