package com.example.ai_caht

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide

class PlayActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, status())
                .commit()

        val button_bar4 = findViewById<LottieAnimationView>(R.id.button_bar4)
        val button_bar2 = findViewById<LottieAnimationView>(R.id.button_bar2)
        val button_bar1 = findViewById<LottieAnimationView>(R.id.button_bar1)

        val feed = findViewById<TextView>(R.id.feed)
        val feedimg = findViewById<ImageView>(R.id.feedimg)
        val status = findViewById<TextView>(R.id.status)
        val statusimg = findViewById<ImageView>(R.id.statusimg)
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
            button_bar2.setVisibility(View.VISIBLE)
            button_bar2.playAnimation()
            button_bar1.setVisibility(View.INVISIBLE)
            button_bar4.setVisibility(View.INVISIBLE)
        })

        feedimg.setOnClickListener({
            supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, feed())
                .commit()
            feedimg.setImageResource(R.drawable.underbar_food1)
            statusimg.setImageResource(R.drawable.underbar_parrot)
            button_bar2.setVisibility(View.VISIBLE)
            button_bar2.playAnimation()
            button_bar1.setVisibility(View.INVISIBLE)
            button_bar4.setVisibility(View.INVISIBLE)
        })

        status.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, status())
                    .commit()
            statusimg.setImageResource(R.drawable.underbar_parrot1)
            feedimg.setImageResource(R.drawable.underbar_food)
            button_bar1.setVisibility(View.VISIBLE)
            button_bar1.playAnimation()
            button_bar2.setVisibility(View.INVISIBLE)
            button_bar4.setVisibility(View.INVISIBLE)
        })

        statusimg.setOnClickListener({
            supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, status())
                .commit()
            statusimg.setImageResource(R.drawable.underbar_parrot1)
            feedimg.setImageResource(R.drawable.underbar_food)
            button_bar1.setVisibility(View.VISIBLE)
            button_bar1.playAnimation()
            button_bar2.setVisibility(View.INVISIBLE)
            button_bar4.setVisibility(View.INVISIBLE)
        })

        converimg.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.list, conversation())
                    .commit()
            converimg.setImageResource(R.drawable.underbar_conver1)
            feedimg.setImageResource(R.drawable.underbar_food)
            statusimg.setImageResource(R.drawable.underbar_parrot)
            button_bar4.setVisibility(View.VISIBLE)
            button_bar4.playAnimation()
            button_bar2.setVisibility(View.INVISIBLE)
            button_bar1.setVisibility(View.INVISIBLE)
        })


        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.ani1).into(ani1)




    }
}