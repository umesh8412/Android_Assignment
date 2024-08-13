package com.umesh.androidassignment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesManager: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferencesManager = SharedPreferences(this)
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"


        sharedPreferencesManager.saveBearerToken(token)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNavView.setupWithNavController(navController)
        bottomNavView.setOnItemSelectedListener { item ->
            val selectedFragment: Int = when (item.itemId) {
                R.id.linksFragment -> R.id.fragmentLinks
                R.id.btm_nav_campaign -> R.id.campaignFragment
                R.id.btm_nav_course->R.id.coursesFragment
                R.id.btm_nav_profile->R.id.profileFragment
                else -> R.id.fragmentLinks
            }
            navController.popBackStack()
            navController.navigate(selectedFragment)
            true
        }
    }
}