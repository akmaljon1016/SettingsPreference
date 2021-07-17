package com.example.settingpreference

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import java.util.*


const val CHANGE_LANGUAGE = "change_language"
const val CHANGE_THEME = "change_theme"

class SettingsFragment : PreferenceFragmentCompat() {
    var listenr: chaqir? = null

    lateinit var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { preference, key ->

                if (key.equals(CHANGE_LANGUAGE)) {
                    var conPref: Preference? = key?.let { findPreference(it) }
                    conPref?.summary = preference?.getString(key, "")
//                    Toast.makeText(context, "saasasa", Toast.LENGTH_SHORT).show()
//                    setLocalisation(key)
                    preference?.getString(key, "")?.let { setLocalisation(it) }
                    val intent: Intent? =
                        requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
                            ?.apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            }
                    startActivity(intent)
                    listenr?.refresh(key)
                }

                /**Theme**/
                if (preference.getBoolean(CHANGE_THEME, false)) {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                /**Theme**/
            }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(
            preferenceChangeListener
        )
        val conPref: Preference? = findPreference(CHANGE_LANGUAGE)
        conPref?.setSummary(preferenceScreen.sharedPreferences.getString(CHANGE_LANGUAGE, ""))
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(
            preferenceChangeListener
        )
    }

    fun setLocale(language: String) {
        val myLocale: Locale = Locale(language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

    interface chaqir {
        fun refresh(key: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenr = context as chaqir
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
}