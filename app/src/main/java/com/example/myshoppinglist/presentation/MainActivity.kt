package com.example.myshoppinglist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppinglist.R
import com.example.myshoppinglist.databinding.ActivityMainBinding
import com.example.myshoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
//     var viewModel: MainActivityViewModel by viewModels() TODO() почему так не работает

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var llShopItem: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        llShopItem = binding.llShopItem
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.shopList.observe(this) {
            showList(it)

        }
    }

    private fun showList(list: List<ShopItem>) {
        llShopItem.removeAllViews()
        for (el in list) {
            val card = if (el.enabled)
                R.layout.item_shop_enabled
            else
                R.layout.item_shop_disabled

            val createView = LayoutInflater.from(this).inflate(card, llShopItem, false)

            val count = createView.findViewById<TextView>(R.id.tv_count)
            count.text = el.count.toString()

            val name = createView.findViewById<TextView>(R.id.tv_name)
            name.text = el.name

            createView.setOnLongClickListener {
                viewModel.changeEnableState(el)
                true
            }
                llShopItem.addView(createView)

        }
    }
}