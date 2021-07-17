package com.example.settingpreference

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import java.util.*

class SettingsActivity : AppCompatActivity(), SettingsFragment.chaqir {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState != null) {
            return
        }
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, SettingsFragment())
            .commit()
    }

    override fun refresh(key: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("key",key)
        }
        startActivity(intent)
    }

    fun setLocalisation(language: String) {
        val local: Locale = Locale(language)
        Locale.setDefault(local)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(local)
        res.updateConfiguration(conf, dm)
    }
}