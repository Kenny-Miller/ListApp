package com.example.listapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.models.Item


public class ItemListAdapter (context: Context, val items: List<Item>): RecyclerView.Adapter<ItemListAdapter.ItemHolder>(){

    inner class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var itemText: TextView = itemView.findViewById<TextView>(R.id.item_text);

        init{
            itemView.setOnClickListener(this);
        }

        override fun onClick(v: View) {
            val apos = bindingAdapterPosition
            val item = items[apos]
            item.isSelected = !item.isSelected;
            var color = "#FFFFFF";
            if(item.isSelected){
                color = "#CCCCCC"
            }
            itemText.setBackgroundColor(Color.parseColor(color));
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_holder, parent, false);
        return ItemHolder(view);
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item : Item = items[position];
        holder.apply{
            itemText.text = item.text;
            val selected = item.isSelected;
            var color = "#FFFFFF";
            if(selected){
                color = "#CCCCCC"
            }
            itemText.setBackgroundColor(Color.parseColor(color));
        }
    }

    override fun getItemCount(): Int {
        return items.size;
    }
}