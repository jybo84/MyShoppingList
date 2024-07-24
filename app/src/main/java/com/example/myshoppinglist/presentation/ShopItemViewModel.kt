package com.example.myshoppinglist.presentation

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.data.ShopListRepositoryImpl
import com.example.myshoppinglist.domain.AddShopItemUseCase
import com.example.myshoppinglist.domain.EditShopItemUseCase
import com.example.myshoppinglist.domain.GetShopItemUseCase
import com.example.myshoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean> = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem> = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item

    }

    fun addShopItem(inputName: String, inputCount: String) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validate = validateInput(name, count)
        if (validate) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }
    }

    fun editShopItem(inputName: String, inputCount: String) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validate = validateInput(name, count)
        if (validate) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }
    }

    private fun parseName(inputName: String): String {
        return inputName.trim() ?: " "
    }

    private fun parseCount(inputCount: String): Int {
        return try {
            inputCount.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }


    private fun validateInput(inputName: String, inputCount: Int): Boolean {
        if (inputName.isBlank()) {
            _errorInputName.value = true
        }
        if (inputCount <= 0) {
            _errorInputCount.value = true
        }
        return !(errorInputName.value == true || errorInputCount.value == true)
    }


    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}