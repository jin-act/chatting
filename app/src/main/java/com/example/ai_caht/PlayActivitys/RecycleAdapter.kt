package com.example.ai_caht.PlayActivitys

import android.accounts.AccountManager.get
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.style.StrikethroughSpan
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
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
    var selectedItem = -1
    var list = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val text = comments.get(position)
        holder.message.text = text.contents
        holder.image.setImageResource(text.profile)
        holder.layout.gravity = text.position
        holder.time.text = text.time
        holder.image.visibility = text.visibility
        holder.id.text = text.id.toString()
        holder.radio_btn.visibility = text.radio
        holder.layout_box.setBackgroundResource(text.textBox)

        holder.radio_btn.setOnClickListener {
            if(holder.radio_btn.isChecked && !list.contains(position)){
                list.add(position)
                list.sortDescending()
                holder.radio_btn.isChecked = true
                Toast.makeText(context, list.toString(), Toast.LENGTH_SHORT).show()
            }
            else{
                list.remove(position)
                list.sortDescending()
                holder.radio_btn.isChecked = false
                Toast.makeText(context, list.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnLongClickListener {
            var data = ""
            for(i :Int in 0 until itemCount){
                helper?.update_db(comments.get(i))
                comments.clear()
                comments.addAll(helper!!.select_db())
                notifyDataSetChanged()
            }
            true
        }
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
        val chatRequest = ChatRequest(chat)
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

class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
    val message: TextView = view.findViewById(R.id.tv_name)
    val image: ImageView = view.findViewById(R.id.chat_Image)
    val layout: LinearLayout = view.findViewById(R.id.layout_pos)
    val time: TextView = view.findViewById(R.id.time)
    val id: TextView = view.findViewById(R.id.id_num)
    val layout_box: LinearLayout = view.findViewById(R.id.layout_box)
    val radio_btn: RadioButton = view.findViewById(R.id.radio_btn)
    //val layout_main: LinearLayout = view.findViewById(R.id.layout_main)

}
