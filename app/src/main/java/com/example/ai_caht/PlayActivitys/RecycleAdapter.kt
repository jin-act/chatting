package com.example.ai_caht.PlayActivitys

import android.accounts.AccountManager.get
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.text.style.StrikethroughSpan
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.example.ai_caht.PlayActivity
import com.example.ai_caht.R
import com.example.ai_caht.databinding.ListItemBinding
import com.example.ai_caht.test.Chat.AdapterRequest
import com.example.ai_caht.test.Chat.AdapterResponse
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
    var list = ArrayList<Int>()
    var data = ArrayList<String>()
    var id = ArrayList<Long>()
    var rdoCheck :Boolean = false
    var check :Boolean = false
    private val activity : ChatActivity = context as ChatActivity

    @Override
    override fun getItemViewType(position: Int): Int {
        return position
    }

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
        holder.textTime.gravity = text.timeText

        holder.radio_btn.setOnClickListener {
            activity.delete_button_set()
            if(holder.radio_btn.isChecked && !list.contains(position)){
                list.add(position)
                data.add(comments.get(position).contents)
                comments.get(position).id?.let { it1 -> id.add(it1) }
                list.sortDescending()
                holder.radio_btn.isChecked = true
                //Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
            else{
                list.remove(position)
                list.sortDescending()
                data.remove(comments.get(position).contents)
                id.remove(comments.get(position).id)
                holder.radio_btn.isChecked = false
                //Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnLongClickListener {
            rdoCheck = true

            radiobtn_on()
            true
            /*
            for(i :Int in 0 until itemCount){
                rdoCheck = true
                //helper?.update_db(comments.get(i))
                radiobtn_on(comments.get(i))
                comments.clear()
                //comments.addAll(helper!!.select_db())
                select_chat()
                notifyDataSetChanged()
            }
            true
            */
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun radiobtn_on(){
        var initMyApi = RetrofitClient.getRetrofitInterface()
        val userId = MySharedPreferences.getUserId(context)
        initMyApi.selectAll(userId)?.enqueue(object : Callback<List<AdapterResponse>> {
            override fun onResponse(call: Call<List<AdapterResponse>>, response: Response<List<AdapterResponse>>) {
                //통신 성공
                if (response.isSuccessful) {
                    val list = mutableListOf<ChatLayout>()
                    val body = response.body()
                    if (body != null) {
                        for(i in body) {
                            val id = i.id
                            val profile = i.profile
                            val contents = i.contents
                            val position = i.position
                            val time = i.time
                            val visibility = i.visibility
                            val textBox = i.textBox
                            val radio = View.VISIBLE
                            val timeText = i.timeText
                            list.add(ChatLayout(id, profile, contents, position, time, visibility, textBox, radio, timeText))
                            println(list.toString())
                            comments.clear()
                            comments.addAll(list)
                        }
                    }
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<AdapterResponse>?>, t: Throwable) {
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
    val textTime: LinearLayout = view.findViewById(R.id.layout_time)

    //val layout_main: LinearLayout = view.findViewById(R.id.layout_main)

}
