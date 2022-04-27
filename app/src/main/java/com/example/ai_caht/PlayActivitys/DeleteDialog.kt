package com.example.ai_caht.PlayActivitys
import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import com.example.ai_caht.R

class DeleteDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener){
        onClickListener = listener
    }

    fun showDialog(){
        dialog.setContentView(R.layout.fragment_coversationdelete)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val btn = dialog.findViewById<Button>(R.id.delete_btn)
        btn.setOnClickListener {
            onClickListener.onClicked("delete")
            dialog.dismiss()
        }
    }
    interface OnDialogClickListener{
        fun onClicked(name: String)
    }
}