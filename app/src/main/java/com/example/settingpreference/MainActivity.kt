package com.example.settingpreference

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

const val TAG = "FLOW"

class MainActivity : AppCompatActivity() {
    lateinit var flow: Flow<Int>
    lateinit var preference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var preference = PreferenceManager.getDefaultSharedPreferences(baseContext)
        if (preference.getBoolean("change_theme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        preference.getString("change_language", "en")?.let { setLocalisation(it) }
        setContentView(R.layout.activity_main)

        setupFlow()
        setupClicks()
    }

    private fun setupClicks() {
        btn_change.setOnClickListener {
//            CoroutineScope(Dispatchers.Main).launch {
//                flow.collect {
//                    Log.d(TAG, it.toString())
//                }
//            }
            network()
        }
    }

    private fun setupFlow() {
        flow = flow {
            Log.d(TAG, "Start flow")
            (0..10).forEach {
                delay(500)
                Log.d(TAG, "Emitting $it")
                emit(it)
            }
        }.map {
            it * it
        }.flowOn(Dispatchers.Default)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLocalisation(language: String) {
        val local: Locale = Locale(language)
        Locale.setDefault(local)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(local)
        res.updateConfiguration(conf, dm)
    }

    fun network(){
        val t:NetworkResult<String>
        val a:String=""
        if (a.isNotEmpty()){
          t=NetworkResult.Success("Success")
        }
        else{
            t=NetworkResult.Error("Error")
        }

        val message=when(t){
            is NetworkResult.Error -> Toast.makeText(this, "Errorr", Toast.LENGTH_SHORT).show()
            is NetworkResult.Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            is NetworkResult.Success -> Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }
}