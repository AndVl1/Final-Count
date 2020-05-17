package com.techpark.finalcount.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.techpark.finalcount.R
import com.techpark.finalcount.adding.views.activity.AddingActivity
import com.techpark.finalcount.databinding.ActivityMainBinding
import com.techpark.finalcount.history.views.fragment.HistoryFragment
import com.techpark.finalcount.main.ui.plans.PlansFragment
import com.techpark.finalcount.main.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

	private lateinit var mainBinding: ActivityMainBinding
	private val screens: Array<Fragment> = arrayOf(
		HistoryFragment(),
		PlansFragment(),
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

		mainBinding.fab.setOnClickListener {
			startActivity(Intent(applicationContext, AddingActivity::class.java))
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.main_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.save_csv -> {
				
				return true
			}
		}
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
