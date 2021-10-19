package com.example.listapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.models.Item
import com.example.listapp.models.ItemList
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var itemList = ItemList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        getData(itemList,ladapter)
    }

    fun getData(itemList:ItemList, lAdapter: ItemListAdapter){
        val nst = Executors.newSingleThreadExecutor()
        nst.execute {
            var response = URL("http://mec402.boisestate.edu/cgi-bin/cs402/lenjson")
            var jsText = response.readText()
            var json = JSONObject(jsText)
           // val itemCount = json.getString("length").toInt()
            val itemCount = 100
            for(i in 0 until itemCount/10){
                //Log.d("times",i.toString())
                val start = i * 10
                var stop = (i+1)*10
                if(i+1 == itemCount/10){
                    stop = itemCount
                }
                response = URL("http://mec402.boisestate.edu/cgi-bin/cs402/pagejson?start=${start}&stop=${stop}")
                jsText = response.readText()
                json = JSONObject(jsText)
                val items = json.getJSONArray("data")

                val listCount = itemList.size
                for(j in 0 until items.length()){
                    val item = items.getJSONObject(j)
                    itemList.add(Item(item.getString("name"),item.getBoolean("selected")))
                }
                runOnUiThread(Runnable {
                    lAdapter.notifyItemRangeInserted(listCount,items.length()-1)
                })

            }
        }

    }
}