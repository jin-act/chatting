package com.example.ai_caht

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val ani1 = findViewById<ImageView>(R.id.ani1)
        Glide.with(this).load(R.raw.animated_stand).into(ani1)
    }
}