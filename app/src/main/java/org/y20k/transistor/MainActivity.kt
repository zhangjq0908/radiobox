/*
 * MainActivity.kt
 * Implements the MainActivity class
 * MainActivity is the default activity that can host the player fragment and the settings fragment
 *
 * This file is part of
 * TRANSISTOR - Radio App for Android
 *
 * Copyright (c) 2015-23 - Y20K.org
 * Licensed under the MIT-License
 * http://opensource.org/licenses/MIT
 */


package org.y20k.transistor

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import org.y20k.transistor.helpers.AppThemeHelper
import org.y20k.transistor.helpers.FileHelper
import org.y20k.transistor.helpers.ImportHelper
import org.y20k.transistor.helpers.PreferencesHelper


/*
 * MainActivity class
 */
class MainActivity: AppCompatActivity() {

    /* Define log tag */
    private val TAG: String = MainActivity::class.java.simpleName


    /* Main class variables */
    private lateinit var appBarConfiguration: AppBarConfiguration


    /* Overrides onCreate from AppCompatActivity */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PreferencesHelper.isHouseKeepingNecessary()) {
            // house-keeping 1: remove hard coded default image
            ImportHelper.removeDefaultStationImageUris(this)
            // house-keeping 2: if existing user detected, enable Edit Stations by default
            if (PreferencesHelper.loadCollectionSize() != -1) {
                // existing user detected - enable Edit Stations by default
                PreferencesHelper.saveEditStationsEnabled(true)
            }
            PreferencesHelper.saveHouseKeepingNecessaryState()
        }

        // set up views
        setContentView(R.layout.activity_main)

        // create .nomedia file - if not yet existing
        FileHelper.createNomediaFile(getExternalFilesDir(null))

        // set up action bar
        setSupportActionBar(findViewById(R.id.main_toolbar))
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        supportActionBar?.hide()

        // register listener for changes in shared preferences
        PreferencesHelper.registerPreferenceChangeListener(sharedPreferenceChangeListener)
    }


    /* Overrides onSupportNavigateUp from AppCompatActivity */
    override fun onSupportNavigateUp(): Boolean {
        // Taken from: https://developer.android.com/guide/navigation/navigation-ui#action_bar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    /* Overrides onDestroy from AppCompatActivity */
    override fun onDestroy() {
        super.onDestroy()
        // unregister listener for changes in shared preferences
        PreferencesHelper.unregisterPreferenceChangeListener(sharedPreferenceChangeListener)
    }


    /*
     * Defines the listener for changes in shared preferences
     */
    private val sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            Keys.PREF_THEME_SELECTION -> {
                AppThemeHelper.setTheme(PreferencesHelper.loadThemeSelection())
            }
        }
    }
    /*
     * End of declaration
     */

}
