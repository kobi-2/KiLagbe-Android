package com.kilagbe.kilagbe.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.kilagbe.kilagbe.R

class CustomerHome : AppCompatActivity() {

    lateinit var nav: NavigationView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var actv: AutoCompleteTextView

    private var suggestions = arrayOf("Mueeze", "Ehsan", "Fayad", "Galib", "Galiba", "Ehtesham", "Mubin", "Farhan", "Maruf")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_home)

        nav = findViewById(R.id.nav_menu)
        drawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        actv = findViewById(R.id.actv_customer)
        val adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, suggestions)
        actv.threshold = 1
        actv.setAdapter(adapter)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navController = findNavController(R.id.nav_host_fragment)

        nav.setupWithNavController(navController)
    }
}
