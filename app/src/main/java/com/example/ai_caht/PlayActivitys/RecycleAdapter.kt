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
    var list = ArrayList<Int>()
    var data = ArrayList<String>()
    var rdoCheck :Boolean = false

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
            if(holder.radio_btn.isChecked && !list.contains(position)){
                list.add(position)
                data.add(comments.get(position).contents)
                list.sortDescending()
                holder.radio_btn.isChecked = true
                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
            else{
                list.remove(position)
                list.sortDescending()
                data.remove(comments.get(position).contents)
                holder.radio_btn.isChecked = false
                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnLongClickListener {
            for(i :Int in 0 until itemCount){
                rdoCheck = true
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
