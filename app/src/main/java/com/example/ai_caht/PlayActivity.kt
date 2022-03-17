package com.example.ai_caht

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.ai_caht.PlayActivitys.ChatActivity
import com.example.ai_caht.test.Login.LoginActivity2
import com.google.android.material.snackbar.Snackbar

class PlayActivity : AppCompatActivity() {
    var mBackWait:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, status())
                .commit()


        val feed = findViewById<TextView>(R.id.feed)
        val feedimg = findViewById<ImageView>(R.id.feedimg)
        val status = findViewById<TextView>(R.id.status)
        val statusimg = findViewById<ImageView>(R.id.statusimg)
        val conver = findViewById<LinearLayout>(R.id.conver)
        val converimg = findViewById<ImageView>(R.id.converimg)

        val account = findViewById<ImageView>(R.id.account)


        account.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.list,list())
                    .commit()
        })


        feed.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, feed())
                    .commit()
            feedimg.setImageResource(R.drawable.underbar_food1)
            statusimg.setImageResource(R.drawable.underbar_parrot)

        })

        feedimg.setOnClickListener({
            supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, feed())
                .commit()
            feedimg.setImageResource(R.drawable.underbar_food1)
            statusimg.setImageResource(R.drawable.underbar_parrot)

        })

        status.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, status())
                    .commit()
            statusimg.setImageResource(R.drawable.underbar_parrot1)
            feedimg.setImageResource(R.drawable.underbar_food)

        })

        statusimg.setOnClickListener({
            supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, status())
                .commit()
            statusimg.setImageResource(R.drawable.underbar_parrot1)
            feedimg.setImageResource(R.drawable.underbar_food)

        })

        conver.setOnClickListener({
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            converimg.setImageResource(R.drawable.underbar_conver1)
            feedimg.setImageResource(R.drawable.underbar_food)
            statusimg.setImageResource(R.drawable.underbar_parrot)

        })


        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.ani1).into(ani1)
    }
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
        } else {
            ActivityCompat.finishAffinity(this);
        }
    }
}