package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import com.example.ai_caht.databinding.ActivityDrawBinding

class DrawActivity:AppCompatActivity(){
    lateinit var binding :ActivityDrawBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_draw)
        //setContentView(drawView(this))
        var btn_back = findViewById<ImageView>(R.id.backspace)
        var out_btn = findViewById<TextView>(R.id.out)
        var pencil = findViewById<ImageView>(R.id.draw)
        var modify = findViewById<ImageView>(R.id.modify)
        var refresh = findViewById<ImageView>(R.id.refresh)

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
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }

    }
    /*
    fun save(view: View){
        val bitmap = viewToBitmap(view)
        Toast.makeText(this, Build.VERSION.SDK_INT.toString(), Toast.LENGTH_SHORT).show()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            savePicture_overQ(bitmap)
            Toast.makeText(this, "이미지 저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            savePicture_underQ(bitmap)
            Toast.makeText(this, "이미지 저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun viewToBitmap(view: View): Bitmap{
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun savePicture_overQ(bitmap: Bitmap){
        val fileName = "picture.png"
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try{
            if(uri != null){
                val image = contentResolver.openFileDescriptor(uri, "w", null)
                if(image != null){
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, contentValues, null, null)
                }
            }
        } catch(e: FileNotFoundException){
            e.printStackTrace()
        }
    }

    fun savePicture_underQ(bitmap: Bitmap){
        val fileName = "picture.png"
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/DCIM/imageSave"
        val dir = File(path)

        if(!dir.exists()){
            dir.mkdirs()
        }
        try{
            val fileItem = File("$dir/$fileName")
            fileItem.createNewFile()
            val fos = FileOutputStream(fileItem)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileItem)))

        }catch(e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
    */
}
