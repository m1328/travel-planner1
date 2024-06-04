package com.example.travelplanner.viewmodel
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.travelplanner.data.PackingListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PackingListViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("packing_list_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _items = MutableLiveData<MutableList<PackingListItem>>(loadItemsFromPrefs())
    val items: LiveData<MutableList<PackingListItem>> = _items

    fun addItem(newItem: String) {
        if (newItem.isNotBlank()) {
            val currentList = _items.value ?: mutableListOf()
            currentList.add(PackingListItem(text = newItem))
            _items.value = currentList
            saveItemsToPrefs(currentList)
        }
    }

    fun toggleItemChecked(index: Int) {
        val currentList = _items.value ?: mutableListOf()
        val item = currentList[index]
        item.isChecked = !item.isChecked
        _items.value = currentList
        saveItemsToPrefs(currentList)
    }

    fun removeItem(index: Int) {
        val currentList = _items.value ?: mutableListOf()
        currentList.removeAt(index)
        _items.value = currentList
        saveItemsToPrefs(currentList)
    }

    private fun loadItemsFromPrefs(): MutableList<PackingListItem> {
        val json = sharedPreferences.getString("packing_list_items", "") ?: ""
        return gson.fromJson(json, object : TypeToken<MutableList<PackingListItem>>() {}.type)
            ?: mutableListOf()
    }

    private fun saveItemsToPrefs(items: MutableList<PackingListItem>) {
        val json = gson.toJson(items)
        sharedPreferences.edit().putString("packing_list_items", json).apply()
    }
}