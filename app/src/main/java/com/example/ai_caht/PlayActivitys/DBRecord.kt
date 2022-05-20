package com.example.ai_caht.PlayActivitys

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View

class DBRecord(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version){

    override fun onCreate(db: SQLiteDatabase?){
        val create = "create table DATABASE_RECORD (id integer primary key, date String, feedcount Int, feed Int, Status Int, play Int, playresult Int, chatCounter Int)"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){

    }

    fun insert_db(record:recordLayout) {
        val values = ContentValues()
        values.put("date", record.date)
        values.put("feedcount", record.feedcount)
        values.put("feed", record.feed)
        values.put("Status", record.Status)
        values.put("play", record.play)
        values.put("playresult", record.playresult)
        values.put("chatCounter", record.chatCounter)
        val wd = writableDatabase
        wd.insert("DATABASE_RECORD", null, values)
        wd.close()
    }
    fun insert_date(record:recordLayout){
        val values = ContentValues()
        values.put("date", record.date)
    }

    @SuppressLint("Range")
    fun select_db():MutableList<recordLayout>{
        val list = mutableListOf<recordLayout>()
        val selectAll = "select * from DATABASE_RECORD"
        val rd = readableDatabase
        //시작점 처음 null, 끝점 마지막까지 select * from DATABASE_RECORD
        val cursor = rd.rawQuery(selectAll, null)

        while(cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val feedcount = cursor.getInt(cursor.getColumnIndex("feedcount"))
            val feed = cursor.getInt(cursor.getColumnIndex("feed"))
            val Status = cursor.getInt(cursor.getColumnIndex("Status"))
            val play = cursor.getInt(cursor.getColumnIndex("play"))
            val playresult = cursor.getInt(cursor.getColumnIndex("playresult"))
            val chatCounter = cursor.getInt(cursor.getColumnIndex("chatCounter"))
            list.add(recordLayout(id, date, feedcount, feed, Status, play, playresult, chatCounter))
        }
        cursor.close()
        rd.close()

        return list
    }

    fun delete_db(record: recordLayout){
        //val delete = "delete from android_chatting where id = $position"
        val wd = writableDatabase
        wd.delete("DATABASE_RECORD", "id=${record.id}", null)
        wd.close()
    }

    fun clear_db(){
        val wd = writableDatabase
        wd.delete("DATABASE_RECORD",null,null)
    }

    fun update_db(record: recordLayout){
        //var query = "UPDATE android_database SET contents = '삭제된 메세지 입니다' WHERE id = ${chat.id}"
        val values = ContentValues()
        values.put("date", record.date)
        values.put("feedcount", record.feedcount)
        values.put("feed", record.feed)
        values.put("Status", record.Status)
        values.put("play", record.play)
        values.put("playresult", record.playresult)
        values.put("chatCounter", record.chatCounter)
        val wd = writableDatabase
        wd.update("DATABASE_RECORD", values, "id=${record.id}", null)
        wd.close()
    }

}