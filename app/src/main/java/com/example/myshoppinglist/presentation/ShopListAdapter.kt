package com.example.myshoppinglist.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

    var shopList = listOf<ShopItem>()

    class ShopListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_shop_disabled, parent, false)
        return ShopListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled)
            VIEW_TYPE_ENABLED
            else
                VIEW_TYPE_DISABLED
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val shopItem = shopList[position]
        val status = if (shopItem.enabled)
            "active"
        else
            "not active"
        if (shopItem.enabled) {
            holder.tvName.text = "${shopItem.name} $status"
            holder.tvCount.text = shopItem.count.toString()
            holder.tvName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    android.R.color.holo_purple
                )
            )
        } else {
            holder.tvName.text = ""
            holder.tvCount.text = ""
            holder.tvName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    android.R.color.black
                )
            )
        }
    }

    companion object{
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
    }
}

