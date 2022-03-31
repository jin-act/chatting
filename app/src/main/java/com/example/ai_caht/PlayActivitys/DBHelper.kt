package com.example.ai_caht.PlayActivitys
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version){

    override fun onCreate(db: SQLiteDatabase?){
        val create = "create table android_chat (profile Int, contents String, position Int, time String, visibility Int)"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){

    }

    fun insert_db(chat:ChatLayout) {
        val values = ContentValues()
        values.put("profile", chat.profile)
        values.put("contents", chat.contents)
        values.put("position", chat.position)
        values.put("time", chat.time)
        values.put("visibility", chat.visibility)
        val wd = writableDatabase
        wd.insert("android_chat", null, values)
        wd.close()
    }

    @SuppressLint("Range")
    fun select_db():MutableList<ChatLayout>{
        val list = mutableListOf<ChatLayout>()
        val selectAll = "select * from android_chat"
        val rd = readableDatabase
        val cursor = rd.rawQuery(selectAll, null)
        while(cursor.moveToNext()){
            val profile = cursor.getInt(cursor.getColumnIndex("profile"))
            val contents = cursor.getString(cursor.getColumnIndex("contents"))
            val position = cursor.getInt(cursor.getColumnIndex("position"))
            val time = cursor.getString(cursor.getColumnIndex("time"))
            val visibility = cursor.getInt(cursor.getColumnIndex("visibility"))
            list.add(ChatLayout(profile, contents, position, time, visibility))
        }
        cursor.close()
        rd.close()

        return list
    }

}