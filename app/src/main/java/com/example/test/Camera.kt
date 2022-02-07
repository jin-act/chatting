package com.example.test


import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat


class Camera : AppCompatActivity() {
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera)
        val btn_camera = findViewById<Button>(R.id.btn_Camera)
        val btn_album = findViewById<Button>(R.id.btnAlbum)
        btn_camera.setOnClickListener(){
            CallCamera()
        }
        btn_album.setOnClickListener(){
            GetAlbum()
        }

    }
    //checkPermission 메서드도 카메라, 저장소등 다른 권한들도 확인이 가능하도록 아래와 같이 수정
    fun checkPermission(permissions: Array<out String>, type: Int): Boolean
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false;
                }
            }
        }

        return true;
    }
    //onRequestPermisiionsResult메서드 내부에서도 기존 카메라 권한을
    //확인할 때와 동일한 방식으로 저장소 접근 권한을 확인
    // 권한 확인 후 권한이 승인되지 않았다면 Toast를 통해 관련 메시지를
    //표시하도록 되어 있다. 경우에 따라서는
    //finish()를 호출하여 권한 승인이 이루어지지 않았을 때
    //앱을 강제 종료하도록 처리할 수도 있다.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        when(requestCode) {
            CAMERA_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            STORAGE_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                        //finish() 앱을 종료함
                    }
                }
            }
        }
    }
    //chickPermission 메서드의 매개변수가 추가되었으므로 기존 카메라의 권한을 확인하기 위해
    // 메서드 호출 부분도 아래와 같이 바꿔야 하며
    // 더불어 저장소에 관한 권한 처리도 동시에 수행할 수 있도록
    //checkPermission 메서드 호출 부분도 추가
    fun CallCamera()
    {
        if (checkPermission(CAMERA, CAMERA_CODE) && checkPermission(STORAGE, STORAGE_CODE)) {
            val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(itt, CAMERA_CODE)
        }
    }
    //onActivityResult 메서드를 수정해 사진을 찍으면 사진 파일을 저장하고
    // 저장된 사진을 불러오도록 한다.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("I'm data" + data)
        var FileName = ""
        val img_view = findViewById<ImageView>(R.id.imageView)
        val Text_view = findViewById<TextView>(R.id.textView)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_CODE -> {
                    if (data?.extras?.get("data") != null) {
                        FileName = RandomFileName()
                        val img = data?.extras?.get("data") as Bitmap
                        val uri = saveFile(FileName, "image/jpeg", img)
                        img_view.setImageURI(uri)
                        Text_view.setText("파일명: " + FileName)
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    img_view.setImageURI(uri)
                    Text_view.setText("파일명: " + FileName)

                }
            }
        }
    }
    fun RandomFileName() : String
    {
        val fineName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fineName
    }
    //촬영된 사진을 저장하기 위해서 파일을 저장하는 메서드 작성
    // 메서드는 파일명과 mime타입, 비트맵을 매개변수로 받고 uri를 반환
    fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri?
    {
        //매서드 내부에서는 MediaStore에 저장할 파일명과 mimeType을 지정
        // MediaStore는 외부 저장소를 관리하는 데이터베이스로서
        // Android Q(10)부터 외부 저장소에 파일을 읽고 쓰기 위해서 필요한 외부 저장소 매개체
        var CV = ContentValues()
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        //만약 다른 곳에서 현재 사용하려는 데이터 사용 요청이 오면
        //이를 무시할 수 있도록 한다. 안정성을 위해 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        //MediaStore에 파일을 저장
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        //위에서 가져온 uri를 통해 파일 스크립터를 가져온다.
        //이 파일스크립터를 통해 파일을 읽거나 쓸 수 있게 되는데
        // 여기서는 작성만 가능하게 설정되어 있다.
        if (uri != null) {
            var scriptor = contentResolver.openFileDescriptor(uri, "w")


            if (scriptor != null) {
                val fos = FileOutputStream(scriptor.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
                //Scriptor를 사용해 FileOutputStream으로 Bitmap파일을 저장
                // 이때 사용된 100은 압축의 정도이며 이 수칙가 클수록 화질이 좋아지지만
                // 그만큼 파일의 크기가 커지게 된다.
                //마지막으로 IS_PENDIG을 0으로 돌리고MediaStore를 초기화한 뒤 uri를 반환
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CV.clear()
                    CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, CV, null, null)

                }
            }
        }

        return uri;
    }
    fun GetAlbum()
    {
        if (checkPermission(STORAGE, STORAGE_CODE)) {
            val itt = Intent(Intent.ACTION_PICK)
            itt.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(itt, STORAGE_CODE)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, subActivity::class.java)
        startActivity(intent)
        finish()
    }
}