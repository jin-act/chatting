package com.example.ai_caht.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.ai_caht.MainActivity
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import java.util.*
import kotlin.concurrent.timer


class login : Fragment() {
    var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity?
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }
    var ct: Context? = null
    var check = 0
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (container != null) {
            ct = container.context
        }
        
        val view:View = inflater.inflate(R.layout.fragment_login, container, false) as ViewGroup
        val btn_login = view.findViewById<Button>(R.id.btn_login)
        val et_id = view.findViewById<EditText>(R.id.et_id)
        val et_pw = view.findViewById<EditText>(R.id.et_pw)
        val text_Timer = view.findViewById<TextView>(R.id.text_timer)
        val autologin = view.findViewById<CheckBox>(R.id.previous)
        val pref = mainActivity?.getSharedPreferences("isFirst", AppCompatActivity.MODE_PRIVATE)
        val first = pref?.getBoolean("isFirst", false)
        if (first == false) {
            Log.d("Is first Time?", "first")
            val editor = pref.edit()
            editor.putBoolean("isFirst", true)
            editor.commit()
            MySharedPreferences.autochecked(ct, "0")
        }
        if (MySharedPreferences.getautochecked(ct) == "0") {
            et_pw.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            //**추가 및 변경**
            //아이디 비번 유효성 검사
            //유효성 문제가 없다면 아이디 비번 웹 통신을 통해서 확인
            et_pw.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                //Enter key Action
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    if (et_id.text.toString() != MySharedPreferences.getUserId(ct)) { // 아이디 데이터 확인
                        check += 1
                    } else if (et_pw.text.toString() != MySharedPreferences.getUserPass(ct)) { // 아이디 패스워드 매칭 확인
                        check += 1
                    } else {
                        if(autologin.isChecked){
                            MySharedPreferences.autochecked(ct, "1")
                        }
                        else if(!autologin.isChecked){
                            MySharedPreferences.autochecked(ct, "0") }
                        val intent = Intent(activity, PlayActivity::class.java)
                        startActivity(intent)
                    }
                    if (check >= 5) {
                        btn_login.setEnabled(false) //버튼 비활성화
                        btn_login.setBackgroundResource(R.drawable.contents_box4)
                        et_id.setEnabled(false)
                        et_id.setBackgroundResource(R.drawable.edittext_background2)
                        et_pw.setEnabled(false)
                        et_pw.setBackgroundResource(R.drawable.edittext_background2)

                        var timerTask: Timer? = null
                        var time = 0
                        timerTask = timer(period = 10) {	// timer() 호출
                            time++	// period=10, 0.01초마다 time를 1씩 증가
                            val sec = (time / 100) + 1	// time/100, 나눗셈의 몫 (초 부분)
                            val Ltime = 5-sec
                            if(sec >= 4){
                                timerTask?.cancel()
                            }
                            // UI조작을 위한 메서드
                            mainActivity?.runOnUiThread {
                                text_Timer.text = "$Ltime 초 후에 다시 시도해 주세요"	// TextView 세팅
                            }
                        }
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
                    true
                } else false
            })
            et_id.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                //Enter key Action
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (et_id.text.toString() != MySharedPreferences.getUserId(ct)) { // 아이디 데이터 확인
                        check += 1
                    } else if (et_pw.text.toString() != MySharedPreferences.getUserPass(ct)) { // 아이디 패스워드 매칭 확인
                        check += 1
                    } else {
                        if(autologin.isChecked){
                            MySharedPreferences.autochecked(ct, "1")
                        }
                        else if(!autologin.isChecked){
                            MySharedPreferences.autochecked(ct, "0") }
                        val intent = Intent(activity, PlayActivity::class.java)
                        startActivity(intent)
                    }
                    if (check >= 5) {
                        btn_login.setEnabled(false) //버튼 비활성화
                        btn_login.setBackgroundResource(R.drawable.contents_box4)
                        et_id.setEnabled(false)
                        et_id.setBackgroundResource(R.drawable.edittext_background2)
                        et_pw.setEnabled(false)
                        et_pw.setBackgroundResource(R.drawable.edittext_background2)

                        var timerTask: Timer? = null
                        var time = 0
                        timerTask = timer(period = 10) {	// timer() 호출
                            time++	// period=10, 0.01초마다 time를 1씩 증가
                            val sec = (time / 100) + 1	// time/100, 나눗셈의 몫 (초 부분)
                            val Ltime = 5-sec
                            if(sec >= 4){
                                timerTask?.cancel()
                            }
                            // UI조작을 위한 메서드
                            mainActivity?.runOnUiThread {
                                text_Timer.text = "$Ltime 초 후에 다시 시도해 주세요"	// TextView 세팅
                            }
                        }
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
                    true
                } else false
            })
            btn_login.setOnClickListener {
                if (et_id.text.toString() != MySharedPreferences.getUserId(ct)) { // 아이디 데이터 확인
                    check += 1
                } else if (et_pw.text.toString() != MySharedPreferences.getUserPass(ct)) { // 아이디 패스워드 매칭 확인
                    check += 1
                } else {
                    if(autologin.isChecked){
                        MySharedPreferences.autochecked(ct, "1")
                    }
                    else if(!autologin.isChecked){
                        MySharedPreferences.autochecked(ct, "0") }
                    val intent = Intent(activity, PlayActivity::class.java)
                    startActivity(intent)
                }
                if (check >= 5) {
                    btn_login.setEnabled(false) //버튼 비활성화
                    btn_login.setBackgroundResource(R.drawable.contents_box4)
                    et_id.setEnabled(false)
                    et_id.setBackgroundResource(R.drawable.edittext_background2)
                    et_pw.setEnabled(false)
                    et_pw.setBackgroundResource(R.drawable.edittext_background2)

                    var timerTask: Timer? = null
                    var time = 0
                    timerTask = timer(period = 10) {	// timer() 호출
                        time++	// period=10, 0.01초마다 time를 1씩 증가
                        val sec = (time / 100) + 1	// time/100, 나눗셈의 몫 (초 부분)
                        val Ltime = 5-sec
                        if(sec >= 4){
                            timerTask?.cancel()
                        }
                        // UI조작을 위한 메서드
                        mainActivity?.runOnUiThread {
                            text_Timer.text = "$Ltime 초 후에 다시 시도해 주세요"	// TextView 세팅
                        }
                    }
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
        } else {
            val intent = Intent(activity, PlayActivity::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return view

    }
}