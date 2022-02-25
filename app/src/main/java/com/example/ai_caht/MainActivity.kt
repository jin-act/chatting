package com.example.ai_caht

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import com.bumptech.glide.Glide
import com.example.ai_caht.Login.login


class MainActivity : AppCompatActivity() {
    var check = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
                .replace(R.id.login_join, login())
                .commit()

        val login = findViewById<TextView>(R.id.login)
        val join = findViewById<TextView>(R.id.join)

        login.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.login_join, com.example.ai_caht.Login.login())
                    .commit()
            login.setTextColor(Color.rgb(255,168,39));
            join.setTextColor(Color.rgb(160,160,160));

        })

        join.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.login_join, com.example.ai_caht.Login.join())
                    .commit()
            join.setTextColor(Color.rgb(255,168,39));
            login.setTextColor(Color.rgb(160,160,160));
        })

        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.ani1).into(ani1)


    }

}