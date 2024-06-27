package com.example.myshoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
//     var viewModel: MainActivityViewModel by viewModels() TODO() почему так не работает

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.shopList.observe(this) {
            Log.i("!!!", it.toString())
            val item = it[0]
            viewModel.deleteShopItem(item)
        }
    }
}