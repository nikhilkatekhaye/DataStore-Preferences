package com.nikhil.datastore_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.nikhil.datastore_android.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var bindingActivity: ActivityMainBinding? = null
    private val binding get() = bindingActivity!!
    private lateinit var dataStore: DataStore<Preferences>
    private val dbName : String =  "DemoDB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStore = createDataStore(name = dbName)

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                saveValueToDatStore(binding.editTextTextKey.text.toString().trim(), binding.editTextTextValue.text.toString().trim())
            }
        }

        binding.buttonGet.setOnClickListener {
            lifecycleScope.launch {
                val value = getValueFromDataStore(binding.editTextGetValue.text.toString().trim())
                Toast.makeText(this@MainActivity, "Value --> ${value ?: "No Value Found"}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveValueToDatStore(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    private suspend fun getValueFromDataStore(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}