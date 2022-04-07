package com.example.ai_caht

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.graphics.Rect
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.ai_caht.PlayActivitys.join
import com.example.ai_caht.PlayActivitys.login
import android.widget.EditText

import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.ai_caht.TimeCheck.ForecdTerminationService

import android.content.Intent





class MainActivity : AppCompatActivity() {
    var check = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login = findViewById<TextView>(R.id.login)
        val join = findViewById<TextView>(R.id.join)

        startService(Intent(this, ForecdTerminationService::class.java))

        login.setTextColor(Color.rgb(255,168,39));
        join.setTextColor(Color.rgb(160,160,160));
        supportFragmentManager.beginTransaction()
                .replace(R.id.login_join, login())
                .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_join, com.example.ai_caht.PlayActivitys.login())
            .commit()


        login.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.login_join, com.example.ai_caht.PlayActivitys.login())
                    .commit()
            login.setTextColor(Color.rgb(255,168,39));
            join.setTextColor(Color.rgb(160,160,160));

        })

        join.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.login_join, com.example.ai_caht.PlayActivitys.join())
                    .commit()
            join.setTextColor(Color.rgb(255,168,39));
            login.setTextColor(Color.rgb(160,160,160));
        })

        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.ani1).into(ani1)
    }
    fun fragmentChange(index: Int) {
        if (index == 1) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.login_join, com.example.ai_caht.PlayActivitys.login()).commit()
        } else if (index == 2) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.login_join, com.example.ai_caht.PlayActivitys.join()).commit()
        }
    }

    //화면 터치 시 키보드 내려감
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}