package com.example.ai_caht.PlayActivitys

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
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
import com.example.ai_caht.test.Login.LoginRequest
import com.example.ai_caht.test.Login.LoginResponse
import com.example.ai_caht.test.RetrofitClient
import com.example.ai_caht.test.initMyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        var userID: String = ""
        var userPW: String = ""
        val intent = Intent(activity, PlayActivity::class.java)
        if (first == false) {
            Log.d("Is first Time?", "first")
            val editor = pref.edit()
            editor.putBoolean("isFirst", true)
            editor.commit()
            MySharedPreferences.autochecked(ct, "0")
        }
        et_pw.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        if (MySharedPreferences.getautochecked(ct) == "1") {
            userID = MySharedPreferences.getUserId(ct)
            userPW = MySharedPreferences.getUserPass(ct)
            val loginRequest = LoginRequest(userID, userPW)
            var retrofitClient = RetrofitClient.getInstance()
            var initMyApi = RetrofitClient.getRetrofitInterface()
            initMyApi.getLoginResponse(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        var headers = response.headers()
                        var authorization : String? = headers.get("Authorization")
                        println(authorization)
                        if(authorization == null){
                            check += 1
                            text_Timer.setText("ID,PW를 다시 확인해 주세요")
                            MySharedPreferences.autochecked(ct, "0")
                        }
                        else{
                            MySharedPreferences.token(ct, authorization)
                            if(activity != null){
                                startActivity(intent)
                            }
                        }
                        //다른 통신을 하기 위해 token 저장
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    check += 1
                    text_Timer.setText("알 수 없는 에러입니다. 통신상태를 확인해 주세요")
                }
            })
        }
        btn_login.setOnClickListener {
            userID = et_id.getText().toString().trim { it <= ' ' }
            userPW = et_pw.getText().toString().trim { it <= ' ' }
            val loginRequest = LoginRequest(userID, userPW)
            var retrofitClient = RetrofitClient.getInstance()
            var initMyApi = RetrofitClient.getRetrofitInterface()

            initMyApi.getLoginResponse(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        var headers = response.headers()
                        var authorization : String? = headers.get("Authorization")
                        println(authorization)
                        if(authorization == null){
                            check += 1
                            text_Timer.setText("ID,PW를 다시 확인해 주세요")
                        }
                        else{
                            MySharedPreferences.token(ct, authorization)
                            if(autologin.isChecked){
                                MySharedPreferences.setUserId(ct, userID)
                                MySharedPreferences.setUserPass(ct, userPW)
                                MySharedPreferences.autochecked(ct, "1")
                            }
                            else if(!autologin.isChecked){
                                MySharedPreferences.autochecked(ct, "0") }
                            startActivity(intent)
                        }
                        //다른 통신을 하기 위해 token 저장


                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    check += 1
                    text_Timer.setText("알 수 없는 에러입니다. 통신상태를 확인해 주세요")
                }
            })
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

        // Inflate the layout for this fragment
        return view
    }
}