package com.example.listapp.models

import android.util.Log
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors

class ItemList : ArrayList<Item>() {
    fun generateData(){
        for(i in 0..5){
            add(Item("$i"))
        }
    }
}