package com.example.blekotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothHealthAppConfiguration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var navController: NavController
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawar_layout)
        navigationView=findViewById(R.id.navigation_view)
        navController=findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)

        appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout)

        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        // drawer layout

        NavigationUI.setupWithNavController(navigationView,navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,appBarConfiguration)
    }


}