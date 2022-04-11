package com.example.ai_caht.PlayActivitys

import android.accounts.AccountManager.get
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.style.StrikethroughSpan
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.example.ai_caht.R
import com.example.ai_caht.databinding.ListItemBinding
import com.example.ai_caht.test.Chat.ChatRequest
import com.example.ai_caht.test.Chat.ChatResponse
import com.example.ai_caht.test.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Array.get
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecycleAdapter(val context: Context) : RecyclerView.Adapter<ChatViewHolder>() {
    var comments = ArrayList<ChatLayout>()
    var helper:DBHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ChatViewHolder(view).apply {
            view.setOnLongClickListener {

                val cursor = adapterPosition
                var data = ""
                data = comments.get(cursor).contents
                ChatResponse(cursor, data)
                //helper?.delete_db(comments.get(cursor))
                //comments.remove(comments.get(cursor))
                helper?.update_db(comments.get(cursor))

                comments.set(adapterPosition, ChatLayout(0, R.drawable.ballon2, "삭제된 메세지 입니다", Gravity.START, "", View.VISIBLE, R.drawable.contents_box5))
                notifyDataSetChanged()
                true
            }
        }
    }

    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ChatViewHolder(binding)
    }
    */
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val text = comments.get(position)
        holder.message.text = text.contents
        holder.image.setImageResource(text.profile)
        holder.layout.gravity = text.position
        holder.time.text = text.time
        holder.image.visibility = text.visibility
        holder.id.text = text.id.toString()
        holder.layout_box.setBackgroundResource(text.textBox)
        //holder.image.setImageResource(comments.get(position).profile)
        //holder.message.text = comments[position].contents
        /*
        if(position/2 == 0){
            //holder.layout_main.gravity = Gravity.RIGHT
            holder.image.setImageResource(R.drawable.user)
        }
        else{
            //holder.layout_main.gravity = Gravity.LEFT
            holder.image.setImageResource(R.drawable.ballon2)
        }
        */
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun ChatResponse(position: Int, data: String){
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM/dd a hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()
        val ChatEdit = "Delete$data"
        val chat: String = ChatEdit.trim { it <= ' ' }
        //String userPassword = passwordText.getText().toString().trim();
        val chatRequest = ChatRequest(chat)
        //retrofit 생성
        var retrofitClient = RetrofitClient.getInstance()
        var initMyApi = RetrofitClient.getRetrofitInterface()
        initMyApi.getChatResponse(chatRequest)?.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(
                    call: Call<ChatResponse>,
                    response: Response<ChatResponse>) {
                //통신 성공
                if (response.isSuccessful) {
                    //val body = response.body()
                    //val aichat = body!!.userchat
                    //val cursor = position
                    //helper?.update_db(comments.get(cursor))
                    //comments.set(position, ChatLayout(0, R.drawable.ballon2, "삭제된 메세지 입니다", Gravity.START, curTime, View.VISIBLE, R.drawable.contents_box5))

                }
            }

            //통신 실패
            override fun onFailure(call: Call<ChatResponse?>, t: Throwable) {
                val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                println(t.message)
                builder.setTitle("알림")
                        .setMessage("예상치 못한 오류입니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show()
            }
        })
    }
}
/*
class ChatViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
    fun setData(text:ChatLayout){
        binding.tvName.text = text.contents
    }
}
*/

class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
    val message: TextView = view.findViewById(R.id.tv_name)
    val image: ImageView = view.findViewById(R.id.chat_Image)
    val layout: LinearLayout = view.findViewById(R.id.layout_pos)
    val time: TextView = view.findViewById(R.id.time)
    val id: TextView = view.findViewById(R.id.id_num)
    val layout_box: LinearLayout = view.findViewById(R.id.layout_box)
    //val layout_main: LinearLayout = view.findViewById(R.id.layout_main)

}