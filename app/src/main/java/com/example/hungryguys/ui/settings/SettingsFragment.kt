package com.example.hungryguys.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.hungryguys.R
import com.example.hungryguys.utills.ActivityUtills

//설정에 추가되는거 키값을 여기다 추가
enum class SettingsList {
    notification, darkmode
}

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var activityUtills: ActivityUtills

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        activityUtills = ActivityUtills(requireActivity())

        val sharedpreferences= PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedpreferences.registerOnSharedPreferenceChangeListener(settingChangeEvent)
    }
    // 설정에 대한 처리는 ActivityUtills에서
    private val settingChangeEvent = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key!!) {
            SettingsList.notification.name ->  activityUtills.setNotification(sharedPreferences.getBoolean(key, false))
            SettingsList.darkmode.name -> activityUtills.setDarkmode(sharedPreferences.getBoolean(key, false))
        }
    }
}