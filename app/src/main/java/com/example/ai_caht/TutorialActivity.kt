package com.example.ai_caht

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStateManagerControl
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.ai_caht.PlayActivitys.TutorialFragment1
import com.example.ai_caht.PlayActivitys.TutorialFragment2

private const val PAGE_NUM = 2

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager)

        val tutorial : ViewPager2 = findViewById(R.id.viewPager_tutorial)
        tutorial.adapter = ScreenSlidePagerAdapter(this)
        tutorial.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //val ani1 = findViewById<ImageView>(R.id.ani1)
        //Glide.with(this).load(R.raw.animated_stand).into(ani1)
    }
    override fun onBackPressed() {
        val tutorial : ViewPager2 = findViewById(R.id.viewPager_tutorial)

        if(tutorial.currentItem == 0) {
            super.onBackPressed()
        }
        else{
            tutorial.currentItem = tutorial.currentItem -1
        }
    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa){
        override fun getItemCount(): Int = PAGE_NUM

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> TutorialFragment1()
                else -> TutorialFragment2()
            }
        }

    }
}