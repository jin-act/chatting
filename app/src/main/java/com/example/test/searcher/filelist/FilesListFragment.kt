package com.example.test.searcher.filelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.searcher.common.FileModel
import com.example.test.searcher.utils.getFileModelsFromFiles
import com.example.test.searcher.utils.getFilesFromPath
import kotlinx.android.synthetic.main.fragment_files_list.*
import java.lang.Exception


class FilesListFragment : Fragment() {

    private lateinit var mFilesAdapter : FilesRecyclerAdapter
    private lateinit var PATH:String

    private lateinit var mCallback: OnItemClickListener
    interface OnItemClickListener{
        fun onClick(fileModel : FileModel)

        fun onLongClick(fileModel : FileModel)

    }
    class Builder{
        var path : String = ""

        fun build(): FilesListFragment{
            val fragment = FilesListFragment()
            val args = Bundle()
            args.putString(ARG_PATH, path)
            fragment.arguments = args;
            return fragment
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mCallback = context as OnItemClickListener
        } catch (e : Exception){
            throw Exception("${context} should implement FilesListFragment.OnItemCLickListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }
    private fun initViews(){
        filesRecyclerView.layoutManager = LinearLayoutManager(context)
        mFilesAdapter = FilesRecyclerAdapter()
        filesRecyclerView.adapter = mFilesAdapter
        updateDate()

        mFilesAdapter.onItemClickListener = {
            mCallback.onClick(it)
        }
        mFilesAdapter.onItemLongClickListener = {
            mCallback.onLongClick(it)
        }
    }
    fun updateDate() {
        val files = getFileModelsFromFiles(getFilesFromPath(PATH))

        if (files.isEmpty()) {
            emptyFolderLayout.visibility = View.VISIBLE
        } else {
            emptyFolderLayout.visibility = View.INVISIBLE
        }

        mFilesAdapter.updateData(files)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
            Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
            return
        }
        PATH = filePath

        initViews()
    }


    companion object {
        private const val ARG_PATH: String = "com.example.test.searcher"
        fun build(block:Builder.()->Unit) = Builder().apply(block).build()
    }





}
