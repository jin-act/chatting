package com.example.ai_caht.PlayActivitys

import android.Manifest
import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.provider.Settings
import android.util.AttributeSet
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import com.example.ai_caht.databinding.ActivityDrawBinding
import com.example.ai_caht.test.Chat.ChatRequest
import com.example.ai_caht.test.Chat.ChatResponse
import com.example.ai_caht.test.Chat.ImageRequest
import com.example.ai_caht.test.Chat.ImageResponse
import com.example.ai_caht.test.RetrofitClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception
import java.util.*

class DrawActivity:AppCompatActivity(){
    lateinit var binding :ActivityDrawBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_draw)
        //setContentView(drawView(this))
        val btn_back = findViewById<ImageView>(R.id.backspace)
        val out_btn = findViewById<TextView>(R.id.out)
        val pencil = findViewById<ImageView>(R.id.draw)
        val modify = findViewById<ImageView>(R.id.modify)
        val refresh = findViewById<ImageView>(R.id.refresh)
        val keyword1 = findViewById<TextView>(R.id.keyword1)
        val keyword2 = findViewById<TextView>(R.id.keyword2)
        val drawCanvas = findViewById<LinearLayout>(R.id.drawCanvas)
        val type = MySharedPreferences.get_type(this)
        when(type.toInt()){
            0 -> {
                keyword1.text = "비행기"
                keyword2.text = "배"
            }
            1 -> {
                keyword1.text = "새"
                keyword2.text = "꽃"
            }
            2 -> {
                keyword1.text = "건물"
                keyword2.text = "자동차"
            }
            3 -> {
                keyword1.text = "컴퓨터"
                keyword2.text = "과일"
            }
        }
        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
            if(isGranted){
                Toast.makeText(this, "허가", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "비허가", Toast.LENGTH_SHORT).show()
                intent.addCategory("android.intent.category.DEFAULT");
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName));
            }
        }
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        btn_back.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }

        pencil.setOnClickListener{
            Toast.makeText(this, "BLACK", Toast.LENGTH_SHORT).show()
        }

        modify.setOnClickListener {
            Toast.makeText(this, "WHITE", Toast.LENGTH_SHORT).show()
        }

        refresh.setOnClickListener{
            val intent = Intent(this, DrawActivity::class.java)
            Toast.makeText(this, "초기화", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(intent)
        }

        out_btn.setOnClickListener {
            MySharedPreferences.set_finish(this, "true")
            val bitmap = viewToBitmap(drawCanvas)
            save(bitmap, "test")
            val encodeData = bitmapEncoding(bitmap)
            val imageRequest = ImageRequest(encodeData, type)
            var initMyApi = RetrofitClient.getRetrofitInterface()
            Toast.makeText(this@DrawActivity, "처리중입니다", Toast.LENGTH_SHORT).show()
            initMyApi.imageSend(imageRequest)?.enqueue(object : Callback<ImageResponse> {
                override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                    //통신 성공
                    if (response.isSuccessful) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            val body = response.body()
                            val image = body?.image
                            if(image == null){
                                MySharedPreferences.set_image(this@DrawActivity, "통신에러")
                            }
                            else {
                                MySharedPreferences.set_image(this@DrawActivity, image)
                            }
                        }, 3000)
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@DrawActivity, PlayActivity::class.java)
                            startActivity(intent)
                        }, 4000)

                    }
                }

                override fun onFailure(call: Call<ImageResponse?>, t: Throwable) {
                    val builder = AlertDialog.Builder(this@DrawActivity)
                    println(t.message)
                    builder.setTitle("알림")
                            .setMessage("예상치 못한 오류입니다.\n 고객센터에 문의바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show()
                }
            })
            // Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    fun viewToBitmap(view: View): Bitmap{
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun save(bitmap: Bitmap, name: String){
        val storage = File(this.cacheDir, "images")
        storage.mkdirs()
        val fileName = "$name.jpg"
        val tempFile = File(storage, fileName)
        tempFile.createNewFile()
        val fo = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fo)
        fo.close()
    }

    fun bitmapEncoding(bitmap: Bitmap): String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)

    }
}
