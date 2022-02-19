package com.example.ai_caht

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)


        val ani = findViewById<ImageView>(R.id.ani)
        Glide.with(this).load(R.raw.ani).into(ani)
    }
}