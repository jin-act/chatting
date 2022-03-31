package com.example.ai_caht.PlayActivitys

import android.accounts.AccountManager.get
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.example.ai_caht.R
import com.example.ai_caht.databinding.ListItemBinding
import java.lang.reflect.Array.get

class RecycleAdapter : RecyclerView.Adapter<ChatViewHolder>() {
    var comments = ArrayList<ChatLayout>()
    var helper:DBHelper? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ChatViewHolder(view)
    }

    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ChatViewHolder(binding)
    }
    */
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val text = comments[position]
        holder.message.text = text.contents
        holder.image.setImageResource(text.profile)
        holder.layout.gravity = text.position
        holder.time.text = text.time
        holder.image.visibility = text.visibility
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
        //val layout_main: LinearLayout = view.findViewById(R.id.layout_main)
}
