package com.example.test.searcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.LoginActivity
import com.example.test.R
import com.example.test.searcher.common.FileModel
import com.example.test.searcher.common.FileType
import com.example.test.searcher.filelist.FilesListFragment
import com.example.test.searcher.filelist.FilesRecyclerAdapter
import com.example.test.subActivity
import kotlinx.android.synthetic.main.fragment_files_list.*
import java.io.File

class TestMainActivity : AppCompatActivity(), FilesListFragment.OnItemClickListener {
    override fun onLongClick(fileModel: FileModel) {
    }

    override fun onClick(fileModel: FileModel) {    // 버튼이 클릭되었을 때
        if(fileModel.fileType == FileType.FOLDER){  //만약 버튼이 폴더였다면
            addFileFragment(fileModel)              // 아래의 함수 addFileFragment실행해서 폴더 아래를 다시 빌드
        }else{
            launchFileIntent(fileModel)             // 만약 파일이었다면 아래의 launchFileIntent실행
        }
    }
    private fun addFileFragment(fileModel : FileModel){ // 버튼 클릭에서 실행되는 함수 FilesListFragment.build를 통해서 리스트를 만든다.
        val filesListFragment = FilesListFragment.build{
            path = fileModel.path
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container,  filesListFragment)
        fragmentTransaction.addToBackStack(fileModel.path)
        fragmentTransaction.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {       // 해당 클래스 실행시 작동
        super.onCreate(savedInstanceState)
        // window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) 상태창을 투명하게 하는 것이라고 한다.
        setContentView (R.layout.activity_secu_explorer)

        if (savedInstanceState == null) {
            val filesListFragment = FilesListFragment.build{
                path = Environment.getExternalStorageDirectory().absolutePath
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.container, filesListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
        }// fileRecycleradapter 연결인 것 같지만 방법을 모르겠음
    }

    fun Context.launchFileIntent(fileModel: FileModel) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = FileProvider.getUriForFile(this, packageName, File(fileModel.path))
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if( supportFragmentManager.backStackEntryCount ==0){
            val intent = Intent(this, subActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}