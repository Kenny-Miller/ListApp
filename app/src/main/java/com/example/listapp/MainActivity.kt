package com.example.listapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.models.Item
import com.example.listapp.models.ItemList

class MainActivity : AppCompatActivity() {
    private var itemList = ItemList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemList.getData()
       //itemList.generateData()
        val recyclerView = findViewById<RecyclerView>(R.id.list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val ladapter = ItemListAdapter(this, itemList)
        recyclerView.adapter = ladapter

        val addButton: Button = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val textEntry = findViewById<EditText>(R.id.text_enter).text;
            if(textEntry.toString().isNotBlank()){
                itemList.add(Item(textEntry.toString().replace(",","")))
                textEntry.clear();
                ladapter.notifyItemInserted(itemList.lastIndex)
                recyclerView.scrollToPosition(itemList.lastIndex)
            }
        }

        val deleteButton : Button = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener{
            for(pos in itemList.lastIndex downTo 0){
                if(itemList[pos].isSelected){
                    itemList.removeAt(pos);
                    ladapter.notifyItemRemoved(pos)
                }
            }
        }

        val joinButton : Button = findViewById(R.id.join_button)
        joinButton.setOnClickListener {
            var text = "";
            var index = -1;
            for (i in itemList.lastIndex downTo 0) {
                if (itemList[i].isSelected) {
                    text = "${itemList[i].text}, " + text
                    index = i
                    itemList.removeAt(i)
                    ladapter.notifyItemRemoved(i)
                }
            }
            if (index >= 0) {
                text = text.substring(0, text.length - 2)
                itemList.add(index, Item(text))
                ladapter.notifyItemInserted(index)
            }
        }

        val splitButton : Button = findViewById(R.id.split_button)
        splitButton.setOnClickListener{
            for(pos in itemList.lastIndex downTo 0){
                if(itemList[pos].isSelected){
                    val items = itemList[pos].split()
                    ladapter.notifyItemChanged(pos)
                    itemList.addAll(pos+1,items)
                    ladapter.notifyItemRangeInserted(pos+1,items.size)
                }
            }
        }
    }
}