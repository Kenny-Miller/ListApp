package com.example.listapp.models

class Item constructor(var text: String, var isSelected : Boolean = false){

    fun split() : List<Item>{
        isSelected = false
        val strings : List<String> = text.split(",")
        val items = arrayListOf<Item>()
        text = strings[0].trim()
        strings.forEachIndexed{pos, it -> if(pos != 0) items.add(Item(it.trim()))}
        return items;
    }
}