package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.ai_caht.R
import android.content.Intent
import androidx.annotation.Nullable
import com.example.ai_caht.MainActivity
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.test.IDduplicateResponse
import com.example.ai_caht.test.RetrofitClient
import com.example.ai_caht.test.Signup.SignupRequest
import com.example.ai_caht.test.Signup.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class join : Fragment() {
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

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            ct = container.context
        }
        val view: View = inflater.inflate(R.layout.fragment_join, container, false) as ViewGroup
        val btn_SignUp = view.findViewById<Button>(R.id.btn_Join)
        val et_joinid = view.findViewById<EditText>(R.id.et_joinid)
        val btn_ID = view.findViewById<Button>(R.id.btn_ID)
        val et_joinpw = view.findViewById<EditText>(R.id.et_joinpw)
        val et_pwcheck = view.findViewById<EditText>(R.id.et_pwcheck)
        val email = view.findViewById<EditText>(R.id.email)
        val btn_email = view.findViewById<Button>(R.id.btn_email)
        val certification = view.findViewById<EditText>(R.id.certification)
        val btn_Join = view.findViewById<Button>(R.id.btn_Join)
        var checkPass = 0                                           // 패스워드 유효성 검사
        var checkId = 0                                             // ID유효성 검사
        var num = 0
        var inputId = ""
        var inputPw = ""
        var inputName = ""
        var userId = ""
        //시작 ID확인 버튼, pw확인 에디트 비활성화
        btn_ID.setEnabled(false)
        et_pwcheck.setEnabled(false)
        et_pwcheck.setBackgroundResource(R.drawable.contents_box2)
        et_joinid.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btn_ID.setEnabled(true)
                btn_ID.setBackgroundResource(R.drawable.contents_box9)
                if (btn_ID.isEnabled) {
                    checkId = 0
                }
            }

            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}

            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                et_joinid.setBackgroundResource(R.drawable.edittext_background)
                et_joinpw.setBackgroundResource(R.drawable.contents_box)
                if (num == 1) {
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box)
                }
            }
        })
        btn_ID.setOnClickListener {
            //**추가**
            // 웹 통신 아이디 중복 검사 및 저장
            if (num == 1) {
                et_pwcheck.setBackgroundResource(R.drawable.contents_box)
            }
            userId = et_joinid.getText().toString()
            var retrofitClient = RetrofitClient.getInstance()
            var initMyApi = RetrofitClient.getRetrofitInterface()
            initMyApi.getidduplicateResponse(userId)
                .enqueue(object : Callback<IDduplicateResponse> {
                    override fun onResponse(
                        call: Call<IDduplicateResponse>,
                        response: Response<IDduplicateResponse>
                    ) {
                        if (response.isSuccessful) {
                            var body = response.body()
                            if (body!!.duplicate == "no") {
                                et_joinid.setBackgroundResource(R.drawable.contents_box7)
                                et_joinpw.setBackgroundResource(R.drawable.contents_box)
                                checkId = 1
                            } else {
                            }
                        }

                    }

                    override fun onFailure(call: Call<IDduplicateResponse>, t: Throwable) {
                    }
                })
        }
        et_joinpw.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // pw체크 초기화
                checkPass = 0
                et_pwcheck.setText("")
                //pw 유효성 검사 유효하면 pw 확인 에디트 텍스트를 활성화
                if (Pattern.matches(
                        "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",
                        et_joinpw.text.toString()
                    )
                ) {
                    et_pwcheck.setEnabled(true)
                    num = 1
                    et_joinpw.setBackgroundResource(R.drawable.contents_box7)
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box)
                } else {
                    et_pwcheck.setEnabled(false)
                    num = 0
                    et_joinpw.setBackgroundResource(R.drawable.edittext_background)
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box2)
                }
            }

            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {
            }

            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                et_joinid.setBackgroundResource(R.drawable.contents_box)
                et_joinpw.setBackgroundResource(R.drawable.edittext_background)
            }
        })
        et_pwcheck.addTextChangedListener(object : TextWatcher {
            // 입력난에 변화가 있을 시 조치
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (Pattern.matches(et_pwcheck.text.toString(), et_joinpw.text.toString())) {
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box7)
                    checkPass = 1
                } else {
                    checkPass = 0
                }

            }

            // 입력이 끝났을 때 조치
            override fun afterTextChanged(arg0: Editable) {}

            // 입력하기 전에 조치
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                et_joinid.setBackgroundResource(R.drawable.contents_box)
                et_joinpw.setBackgroundResource(R.drawable.contents_box)
                et_pwcheck.setBackgroundResource(R.drawable.edittext_background)
            }
        })
        //회원가입 완료버튼 눌렀을 때
        btn_SignUp.setOnClickListener {
            //ID, pw 유효성이 모두 완료되었을 때, 회원가입 완료
            if (checkPass == 1 && checkId == 1) {
                inputId = et_joinid.getText().toString()
                inputPw = et_joinpw.getText().toString()
                inputName = "no_name"
                var signupRequest = SignupRequest(inputId, inputPw, inputName)
                var retrofitClient = RetrofitClient.getInstance()
                var initMyApi = RetrofitClient.getRetrofitInterface()
                initMyApi.getSignupResponse(signupRequest)
                    .enqueue(object : Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            var body = response.body()
                            println(body)
                            if (body == "회원가입완료") {
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                            } else {

                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                        }
                    })
            }
            //ID 유효성이 완료되지 않았을 때
            else if (checkId == 0) {

                et_joinid.setBackgroundResource(R.drawable.contents_box6)
                et_joinpw.setBackgroundResource(R.drawable.contents_box)
                if (num == 1) {
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box)
                }
            } else {
                et_joinid.setBackgroundResource(R.drawable.contents_box)
                et_joinpw.setBackgroundResource(R.drawable.contents_box6)
                if (num == 1) {
                    et_pwcheck.setBackgroundResource(R.drawable.contents_box)
                }
            }
        }
        // Inflate the layout for this fragment
        return view
    }
}