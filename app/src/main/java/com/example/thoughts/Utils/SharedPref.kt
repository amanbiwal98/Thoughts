package com.example.thoughts.Utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {
    fun storeData(
        name: String,
        email: String,
        userName: String,
        bio: String,
        imageUrl: String,
        context: Context,
    ) {
        val sharedPreferences = context.getSharedPreferences("usres", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("userName", userName)
        editor.putString("bio", bio)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getName(context: Context): String{
        val sharedPreferences = context.getSharedPreferences("usres", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!
    }
    fun getEmail(context: Context): String{
        val sharedPreferences = context.getSharedPreferences("usres", MODE_PRIVATE)
        return sharedPreferences.getString("email", "")!!
    }
    fun getuserName(context: Context): String{
        val sharedPreferences = context.getSharedPreferences("usres", MODE_PRIVATE)
        return sharedPreferences.getString("userName", "")!!
    }
    fun getBio(context: Context): String{
        val sharedPreferences = context.getSharedPreferences("usres", MODE_PRIVATE)
        return sharedPreferences.getString("bio", "")!!
    }
    fun getImage(context: Context): String{
        val sharedPreferences = context.getSharedPreferences("usres", MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl", "")!!
    }

}