package com.example.listapp.models

import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors

class ItemList : ArrayList<Item>() {
    fun generateData(){
        for(i in 0..5){
            add(Item("$i"))
        }
    }

    fun getData(){
        Executors.newSingleThreadExecutor().execute{
            val response = URL("http://mec402.boisestate.edu/cgi-bin/dataAccess/getAllAssets.py")
            val jsText = response.readText();
//            val json = JSONObject(jsText)
//            val items = json.getJSONArray("coffee")
//            for(i in 0 until items.length()){
//                val item = items.getJSONObject(i);
//                add(Item(item.getString("name"),item.getBoolean("selected")))
//            }
            add(Item(jsText))
        }
    }
}