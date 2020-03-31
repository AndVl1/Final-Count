package com.techpark.finalcount

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.techpark.finalcount.databinding.ActivityMainBinding
import com.techpark.finalcount.ui.dashboard.DashboardFragment
import com.techpark.finalcount.ui.home.HomeFragment
import com.techpark.finalcount.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val screens: Array<Fragment> = arrayOf(
        HomeFragment(),
        DashboardFragment(),
        ProfileFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> replaceScreen(0)
                R.id.navigation_dashboard -> replaceScreen(1)
                R.id.navigation_profile -> replaceScreen(2)
            }
            true
        }
        replaceScreen(0)
    }

    private fun replaceScreen(position: Int) {
        var pos = position
        if (pos < 0 || pos >= screens.size) {
            pos = 0
        }
        supportFragmentManager
            .beginTransaction()
            .replace(mainBinding.container.id, screens[pos])
            .commit()
    }
}
