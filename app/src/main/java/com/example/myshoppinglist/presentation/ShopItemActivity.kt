package com.example.myshoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppinglist.R
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private var shopItemId = NO_ID
    private var screenMode = MODE_EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EMPTY = "mode_empty"
        private const val NO_ID = -1

        fun newItemAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            Log.d("!!!", "передан парметр ADD")
            return intent
        }

        fun newItemEdiItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            Log.d("!!!", shopItemId.toString())
            return intent
        }
    }

    private fun parseIntent() {
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)

        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("первая ошибка в SIA")
        }

        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("вторая ошибка")
        }
        screenMode = mode

        shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, -1)

        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("третья ошибка")
            }
        }
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("четвертая ошибка")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }
}