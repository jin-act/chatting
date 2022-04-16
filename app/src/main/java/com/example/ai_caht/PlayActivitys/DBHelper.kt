package com.example.ai_caht.PlayActivitys
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import androidx.core.os.persistableBundleOf

class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version){

    override fun onCreate(db: SQLiteDatabase?){
        val create = "create table db_phone (id integer primary key, profile Int, contents String, position Int, time String, visibility Int, textBox Int, radio Int)"
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
        values.put("textBox", chat.textBox)
        values.put("radio", chat.radio)
        val wd = writableDatabase
        wd.insert("db_phone", null, values)
        wd.close()
    }

    @SuppressLint("Range")
    fun select_db():MutableList<ChatLayout>{
        val list = mutableListOf<ChatLayout>()
        val selectAll = "select * from db_phone"
        val rd = readableDatabase
        val cursor = rd.rawQuery(selectAll, null)
        while(cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val profile = cursor.getInt(cursor.getColumnIndex("profile"))
            val contents = cursor.getString(cursor.getColumnIndex("contents"))
            val position = cursor.getInt(cursor.getColumnIndex("position"))
            val time = cursor.getString(cursor.getColumnIndex("time"))
            val visibility = cursor.getInt(cursor.getColumnIndex("visibility"))
            val textBox = cursor.getInt(cursor.getColumnIndex("textBox"))
            val radio = cursor.getInt(cursor.getColumnIndex("radio"))
            list.add(ChatLayout(id, profile, contents, position, time, visibility, textBox, radio))
        }
        cursor.close()
        rd.close()

        return list
    }

    fun delete_db(chat: ChatLayout){
        //val delete = "delete from android_chatting where id = $position"
        val wd = writableDatabase
        wd.delete("db_phone", "id=${chat.id}", null)
        wd.close()
    }

    fun update_db(chat: ChatLayout){
        //var query = "UPDATE android_database SET contents = '삭제된 메세지 입니다' WHERE id = ${chat.id}"
        val values = ContentValues()
        values.put("profile", chat.profile)
        values.put("contents", chat.contents)
        values.put("position", chat.position)
        values.put("time", chat.time)
        values.put("visibility", chat.visibility)
        values.put("textBox", chat.textBox)
        values.put("radio", View.VISIBLE)
        val wd = writableDatabase
        wd.update("db_phone", values, "id=${chat.id}", null)
        wd.close()
    }

    fun update_db2(chat: ChatLayout){
        //var query = "UPDATE android_database SET contents = '삭제된 메세지 입니다' WHERE id = ${chat.id}"
        val values = ContentValues()
        values.put("profile", chat.profile)
        values.put("contents", chat.contents)
        values.put("position", chat.position)
        values.put("time", chat.time)
        values.put("visibility", chat.visibility)
        values.put("textBox", chat.textBox)
        values.put("radio", View.GONE)
        val wd = writableDatabase
        wd.update("db_phone", values, "id=${chat.id}", null)
        wd.close()
    }

}