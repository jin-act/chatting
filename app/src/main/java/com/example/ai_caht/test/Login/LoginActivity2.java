package com.example.ai_caht.test.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_caht.PlayActivity;
import com.example.ai_caht.R;
import com.example.ai_caht.test.BackPressCloseHandler;
import com.example.ai_caht.test.Chat.ChatRequest;
import com.example.ai_caht.test.Chat.ChatResponse;
import com.example.ai_caht.test.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity2 extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private com.example.ai_caht.test.initMyApi initMyApi;
    BackPressCloseHandler backPressCloseHandler;
    EditText idText;
    EditText passwordText;
    Button btn_login;
    CheckBox checkBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        idText = findViewById(R.id.insert_id);
        btn_login = findViewById(R.id.btn_login);
        checkBox = findViewById(R.id.autoLogin);

        //자동 로그인을 선택한 유저
        if (!getPreferenceString("autoLoginId").equals("") && !getPreferenceString("autoLoginPw").equals("")) {
            checkBox.setChecked(true);
            checkAutoLogin(getPreferenceString("autoLoginId"));
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = idText.getText().toString();
                String pw = "1";
                hideKeyboard();

                //로그인 정보 미입력 시
                if (id.trim().length() == 0 || pw.trim().length() == 0 || id == null || pw == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity2.this);
                    builder.setTitle("알림")
                            .setMessage("로그인 정보를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //로그인 통신
                    LoginResponse();
                }
            }
        });
    }

    public void LoginResponse() {
        String userID = idText.getText().toString().trim();
        //String userPassword = passwordText.getText().toString().trim();
        TextView airesult = findViewById(R.id.insert_password);

        //loginRequest에 사용자가 입력한 id와 pw를 저장
        //LoginRequest loginRequest = new LoginRequest(userID, userPassword);
        ChatRequest chatRequest = new ChatRequest(userID);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        initMyApi = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        initMyApi.getChatResponse(chatRequest).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                //통신 성공
                if (response.isSuccessful()) {

                    //response.headers()를 result에 저장
                    /*Headers headers = response.headers();
                    String authorization = headers.get("Authorization");
                    System.out.println("LoginActivity.onResponse");
                    System.out.println("authorization = " + authorization);

                    //다른 통신을 하기 위해 token 저장
                    setPreference("Authorization", authorization);
                    Toast.makeText(LoginActivity2.this, userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
                    intent.putExtra("userId", userID);
                    startActivity(intent);
                    LoginActivity2.this.finish();
                    */

                    ChatResponse body = response.body();
                    String aichat = body.userchat;
                    airesult.setText(aichat);


                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity2.this);
                System.out.println(t.getMessage());
                builder.setTitle("알림")
                        .setMessage("예상치 못한 오류입니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //데이터를 내부 저장소에 저장하기
    public void setPreference(String key, String value) {
        SharedPreferences pref = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //내부 저장소에 저장된 데이터 가져오기
    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        return pref.getString(key, "");
    }


    //키보드 숨기기
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idText.getWindowToken(), 0);
        //imm.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);
    }

    //화면 터치 시 키보드 내려감
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //자동 로그인 유저
    public void checkAutoLogin(String id) {

        //Toast.makeText(this, id + "님 환영합니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        finish();

    }

    //뒤로 가기 버튼 2번 클릭시 종료
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
