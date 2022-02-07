package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.test.databinding.ActivityFeedBinding
import com.example.test.searcher.TestMainActivity
import java.util.*
import kotlin.concurrent.timer

class subActivity : AppCompatActivity() {
    private var mBinding : ActivityFeedBinding? = null
    private val binding get() = mBinding!!
    private var time = 20
    private var hungerCnt = 100
    private var stressCnt = 0
    private var foodArray: Array<String> = arrayOf("가", "나", "다", "라", "마", "바", "사", "아", "자", "차")
    private var selectFood:String? = null
    private var list = mutableListOf<Int>()
    private val random = Random()
    private var rand1 = 100
    private var rand2 = 100
    private var rand3 = 100
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mBinding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProgress()
        startTimer()
        feed()
        binding.btnCamera.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
            finish()
        }

        binding.btntest.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnfoldertest.setOnClickListener {
            val intent = Intent(this, TestMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun feed(){
        binding.txt1.setText("포만감 : " +String.format("%02d", hungerCnt)+ "%\n스트레스 : " +String.format("%02d", stressCnt)+ "%")
        binding.btn1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("먹이주기")
                .setSingleChoiceItems(foodArray, -1){dialog, which ->
                    selectFood = foodArray[which]
                }
                .setPositiveButton("OK"){dialog, which->
                    Toast.makeText(this, "OK",Toast.LENGTH_SHORT)
                    binding.txtFood.setText("선택된 먹이 : "+selectFood)
                    if(selectFood == foodArray[rand1]){
                        binding.prog1.setProgress(0)
                        binding.prog2.setProgress(100)
                        hungerCnt = 100
                        stressCnt = 0
                        binding.txt1.setText("포만감 증가: " +String.format("%02d", hungerCnt)+ "%\n스트레스 감소: " +String.format("%02d", stressCnt)+ "%")
                    }
                    else if(selectFood == foodArray[rand2] || selectFood == foodArray[rand3]){
                        binding.prog1.incrementProgressBy(10)
                        binding.prog2.setProgress(100)
                        hungerCnt = 100
                        stressCnt += 10
                        binding.txt1.setText("포만감 증가: " +String.format("%02d", hungerCnt)+ "%\n스트레스 증가: " +String.format("%02d", stressCnt)+ "%")
                    }
                    else{

                    }
                }
            builder.show()
        }

        binding.btn2.setOnClickListener {
            while(list.size<3){
                val randomNumber = random.nextInt(10)
                if(list.contains(randomNumber)){
                    continue
                }
                list.add(randomNumber)
            }
            rand1 = list.get(0)
            rand2 = list.get(1)
            rand3 = list.get(2)
            binding.txtFood.setText("선호 먹이 : "+foodArray[rand1]+ "\n비선호 먹이 : "+foodArray[rand2]+","+foodArray[rand3])
        }
    }
    private fun setProgress(){
        binding.prog1.setProgress(0)
        binding.prog2.setProgress(100)
    }

    private fun startTimer(){
        timer(period = 1000){
            binding.txt2.setText(String.format("\n%02d", time))
            time --;
            if(time == 0) {
                time = 20
                binding.txt1.setText("포만감 감소 : " +String.format("%02d", hungerCnt)+ "%\n스트레스 : "+String.format("%02d", stressCnt)+ "%")
                binding.prog2.incrementProgressBy(-10)
                hungerCnt -= 10;
                if(hungerCnt <= 0){
                    hungerCnt == 0;
                    stressCnt += 10;
                    binding.txt1.setText("포만감 : " +String.format("%02d", 0)+ "%\n스트레스 증가 : " +String.format("%02d", stressCnt)+ "%")
                    binding.prog1.incrementProgressBy(10)
                }
            }
        }
    }


}