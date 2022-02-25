package com.example.ai_caht

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, status())
                .commit()

        val feed = findViewById<TextView>(R.id.feed)
        feed.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, feed())
                    .commit()
        })

        val feedimg = findViewById<ImageView>(R.id.feedimg)
        feedimg.setOnClickListener({
            supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, feed())
                .commit()
        })

        val status = findViewById<TextView>(R.id.status)
        status.setOnClickListener({
            supportFragmentManager.beginTransaction()
                    .replace(R.id.infospace, status())
                    .commit()
        })
        val statusimg = findViewById<ImageView>(R.id.statusimg)
        statusimg.setOnClickListener({
            supportFragmentManager.beginTransaction()
                .replace(R.id.infospace, status())
                .commit()
        })
        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.ani1).into(ani1)
    }
}