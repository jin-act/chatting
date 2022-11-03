package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.content.SharedPreferences
import java.util.*

object MySharedPreferences {
    val MY_ACCOUNT : String = "account"
    val CHECK_TEST : String = "check"
    val FINISH_TEST : String = "finish"
    val IMAGE_CHECK : String = "image"
    val TYPE_CHECK : String = "type"

    fun autochecked(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("auto", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("AUTO", input)
        editor.commit()
    }
    fun token(context: Context?, input: String?){
        val prefs : SharedPreferences = context!!.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Authorization", input)
        editor.commit()
    }

    fun getautochecked(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("auto", Context.MODE_PRIVATE)
        return prefs.getString("AUTO", "").toString()
    }
    fun setUserId(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("userID", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_ID", input)
        editor.commit()
    }

    fun getUserId(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences("userID", Context.MODE_PRIVATE)
        return prefs.getString("MY_ID", "").toString()
    }

    fun setUserPass(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("userPW", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_PASS", input)
        editor.commit()
    }

    fun getUserPass(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences("userPW", Context.MODE_PRIVATE)
        return prefs.getString("MY_PASS", "").toString()
    }

    fun clearUser(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences("clear", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }
    fun get_Hunger(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("hunger", Context.MODE_PRIVATE)
        return prefs.getString("Hunger", "").toString()
    }
    fun get_Stress(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("stress", Context.MODE_PRIVATE)
        return prefs.getString("Stress", "").toString()
    }
    fun ger_Boredom(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("boredom", Context.MODE_PRIVATE)
        return prefs.getString("Boredom", "").toString()
    }
    fun get_Affection(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("affection", Context.MODE_PRIVATE)
        return prefs.getString("Affection", "").toString()
    }
    fun get_Level(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("level", Context.MODE_PRIVATE)
        return prefs.getString("Level", "").toString()
    }
    fun set_Counter(context: Context?, input : String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("counter", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Counter", input)
        editor.commit()
    }
    fun get_Counter(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("counter", Context.MODE_PRIVATE)
        return prefs.getString("Counter", "").toString()
    }
    fun set_Time(context: Context?, input : Long){
        val prefs : SharedPreferences = context!!.getSharedPreferences("time", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putLong("last_time", input)
        editor.commit()
    }
    fun get_Time(context: Context?): Long {
        val prefs : SharedPreferences = context!!.getSharedPreferences("time", Context.MODE_PRIVATE)
        return prefs.getLong("last_time", 0)
    }

    fun get_food(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("food", Context.MODE_PRIVATE)
        return prefs.getString("food", "").toString()
    }
    fun set_food(context: Context?, food: String?){
        val prefs : SharedPreferences = context!!.getSharedPreferences("food", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("food", food)
        editor.commit()
    }
    fun set_Hunger(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("hunger", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Hunger", input)
        editor.commit()
    }
    fun set_Stress(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("stress", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Stress", input)
        editor.commit()
    }
    fun set_Boredom(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("boredom", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Boredom", input)
        editor.commit()
    }
    fun set_Affection(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("affection", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Affection", input)
        editor.commit()
    }
    fun set_Level(context: Context?, input: String) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("level", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("Level", input)
        editor.commit()
    }
    fun set_check(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences(CHECK_TEST, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("check", input)
        editor.commit()

    }

    fun get_check(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences(CHECK_TEST, Context.MODE_PRIVATE)
        return prefs.getString("check", "").toString()

    }

    fun clear_check(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences(CHECK_TEST, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("check", input)
        editor.commit()
    }

    fun set_finish(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences(FINISH_TEST, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("finish", input)
        editor.commit()
    }

    fun get_finish(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences(FINISH_TEST, Context.MODE_PRIVATE)
        return prefs.getString("finish", "").toString()
    }

    fun set_image(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences(IMAGE_CHECK, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("image", input)
        editor.commit()
    }

    fun get_image(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences(IMAGE_CHECK, Context.MODE_PRIVATE)
        return prefs.getString("image", "").toString()
    }

    fun set_type(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences(TYPE_CHECK, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("type", input)
        editor.commit()
    }

    fun get_type(context: Context?): String {
        val prefs : SharedPreferences = context!!.getSharedPreferences(TYPE_CHECK, Context.MODE_PRIVATE)
        return prefs.getString("type", "").toString()
    }

    fun get_page(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("page", Context.MODE_PRIVATE)
        return prefs.getString("Page", "").toString()
    }
    fun set_page(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("page", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("Page", input)
        editor.commit()
    }
    fun get_date(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("date", Context.MODE_PRIVATE)
        return prefs.getString("Date", "").toString()
    }
    fun set_date(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("date", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("Date", input)
        editor.commit()
    }
    fun get_pState(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("pstate", Context.MODE_PRIVATE)
        return prefs.getString("PState", "").toString()
    }
    fun set_pState(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("pstate", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("PState", input)
        editor.commit()
    }

    fun get_feedCount(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("feedcount", Context.MODE_PRIVATE)
        return prefs.getString("FeedCount", "").toString()
    }
    fun set_feedCount(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("feedcount", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("FeedCount", input)
        editor.commit()
    }

    fun get_feed(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("feed", Context.MODE_PRIVATE)
        return prefs.getString("Feed", "").toString()
    }
    fun set_feed(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("feed", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("Feed", input)
        editor.commit()
    }

    fun get_playType(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("playtype", Context.MODE_PRIVATE)
        return prefs.getString("PlayType", "").toString()
    }
    fun set_playType(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("playtype", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("PlayType", input)
        editor.commit()
    }

    fun get_playResult(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("playresult", Context.MODE_PRIVATE)
        return prefs.getString("PlayResult", "").toString()
    }
    fun set_playResult(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("playresult", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("PlayResult", input)
        editor.commit()
    }

    fun get_chatCount(context: Context?): String{
        val prefs : SharedPreferences = context!!.getSharedPreferences("chatCount", Context.MODE_PRIVATE)
        return prefs.getString("ChatCount", "").toString()
    }
    fun set_chatCount(context: Context?, input: String){
        val prefs : SharedPreferences = context!!.getSharedPreferences("chatCount", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.putString("ChatCount", input)
        editor.commit()
    }
}