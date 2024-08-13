package com.umesh.androidassignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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