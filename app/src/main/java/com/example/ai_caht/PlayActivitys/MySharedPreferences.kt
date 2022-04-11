package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import java.util.*

object MySharedPreferences {
    val MY_ACCOUNT : String = "account"
    fun autochecked(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("AUTO", input)
        editor.commit()
    }
    fun token(context: Context?, input: String?){
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Authorization", input)
        editor.commit()
    }

    fun getautochecked(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("AUTO", "").toString()
    }
    fun setUserId(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_ID", input)
        editor.commit()
    }

    fun getUserId(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MY_ID", "").toString()
    }

    fun setUserPass(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_PASS", input)
        editor.commit()
    }

    fun getUserPass(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MY_PASS", "").toString()
    }

    fun clearUser(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }
    fun set_condition(context: Context?, hunger: String?, boredom:String?, stress: String?, affection: String?, level: String?, counter: String?){
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Hunger", hunger)
        editor.putString("Boredom", boredom)
        editor.putString("Stress", stress)
        editor.putString("Affection", affection)
        editor.putString("Level", level)
        editor.putString("Counter", counter)
        editor.commit()
    }
    fun get_Hunger(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("Hunger", "").toString()
    }
    fun get_Stress(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("Stress", "").toString()
    }
    fun ger_Boredom(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("Boredom", "").toString()
    }
    fun get_Affection(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("Affection", "").toString()
    }
    fun get_Level(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("Level", "").toString()
    }
    fun get_Counter(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("Counter", "").toString()
    }
    fun set_Time(context: Context?, input : Long){
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putLong("last_time", input)
        editor.commit()
    }
    fun get_Time(context: Context?): Long {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getLong("last_time", 0)
    }

    fun get_food(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("food", "").toString()
    }
    fun set_food(context: Context?, food: String?){
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("food", food)
        editor.commit()
    }
    fun set_Hunger(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Hunger", input)
        editor.commit()
    }
    fun set_Stress(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Stress", input)
        editor.commit()
    }
    fun set_Boredom(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Boredom", input)
        editor.commit()
    }
    fun set_Affection(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Affection", input)
        editor.commit()
    }
    fun set_Level(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Level", input)
        editor.commit()
    }
}