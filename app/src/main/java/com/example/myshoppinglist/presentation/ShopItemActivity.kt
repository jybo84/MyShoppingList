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

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button
    private lateinit var viewModel: ShopItemViewModel

    private var shopItem = -1
    private var screenMode = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        initViews()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        parseIntent()
        launchRightMode()
        addTextChangedListener()
        observeViewModels()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        Log.d("!!!", mode.toString())
    }

    private fun observeViewModels() {

        viewModel.errorInputCount.observe(this) { it->
            val message = if (it == true) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = message
        }
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun addTextChangedListener() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"

        fun newItemAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            Log.d("!!!", "add")
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

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        saveButton = findViewById(R.id.save_button)
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("первая ошибка")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("вторая ошибка")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("третья ошибка")
            }
        }
        shopItem = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, -1)
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchAddMode() {
        viewModel.getShopItem(shopItem)
        viewModel.shopItem.observe(this) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        saveButton.setOnClickListener {
            viewModel.addShopItem(etName.toString(), etCount.toString())
        }
    }

    private fun launchEditMode() {
        saveButton.setOnClickListener {
            viewModel.editShopItem(etName.toString(), etCount.toString())
        }
    }
}