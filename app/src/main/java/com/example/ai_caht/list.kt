package com.example.ai_caht

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.ai_caht.PlayActivitys.DBHelper
import com.example.ai_caht.PlayActivitys.MySharedPreferences
import com.example.ai_caht.PlayActivitys.RecycleAdapter


class list : Fragment() {
    var playactivity : PlayActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playactivity = activity as PlayActivity?
    }

    override fun onDetach() {
        super.onDetach()
        playactivity = null
    }
    var ct: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (container != null) {
            ct = container.context
        }
        val helper = DBHelper(ct, "DATABASE_CHAT", null, 1)
        val view:View = inflater.inflate(R.layout.fragment_list, container, false) as ViewGroup
        val logout = view.findViewById<LinearLayout>(R.id.logout)
        val destroy = view.findViewById<LinearLayout>(R.id.destroy)
        logout.setOnClickListener {
            MySharedPreferences.autochecked(ct, "0")
            MySharedPreferences.set_finish(context, "false") // 로그아웃 누르면 임시로 대화 삭제
            helper.clear_db()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        destroy.setOnClickListener{
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }
        return view
    }


}
