package com.example.myshoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopClassDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value

        }

    var onShopItemLongClickListener: OnShopItemLongClickListener? = null
    var onShopItemClickListener: OnShopItemClickListener? = null

    class ShopListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("неизвестный тип")
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
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
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.onShopItemLongClick(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.onShopItemClick(shopItem)
            true
        }
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 10
    }

    interface OnShopItemLongClickListener {
        fun onShopItemLongClick(shopItem: ShopItem)
    }

    interface OnShopItemClickListener {
        fun onShopItemClick(shopItem: ShopItem)
    }
}