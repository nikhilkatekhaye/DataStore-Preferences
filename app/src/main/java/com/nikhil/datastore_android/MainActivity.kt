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

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStore = createDataStore(name = "settings")

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(
                    binding.editTextTextKey.text.toString().trim(),
                    binding.editTextTextValue.text.toString().trim()
                )
            }
        }

        binding.buttonRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding.editTextTextGetValue.text.toString().trim())
                Toast.makeText(this@MainActivity, "Value --> ${value ?: "No Value Found"}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}