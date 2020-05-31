package com.techpark.finalcount.main.views.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.techpark.finalcount.R
import com.techpark.finalcount.adding.plan.views.fragment.AddingPlanningFragment
import com.techpark.finalcount.adding.purchase.views.fragment.AddingPurchaseFragment
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.data.PinPreferences
import com.techpark.finalcount.databinding.ActivityMainBinding
import com.techpark.finalcount.history.views.fragment.HistoryFragment
import com.techpark.finalcount.main.presenters.MainPresenterImpl
import com.techpark.finalcount.main.views.MainView
import com.techpark.finalcount.pincode.views.activity.PincodeActivity
import com.techpark.finalcount.plans.views.fragment.PlansFragment
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView {

	@Inject
	lateinit var mMainPresenter: MainPresenterImpl

	private lateinit var mMainBinding: ActivityMainBinding
	private val mScreens: Array<Fragment> = arrayOf(
		HistoryFragment.newInstance(),
		PlansFragment.newInstance()
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(this)
		super.onCreate(savedInstanceState)
		mMainPresenter.attachView(this)
		mMainBinding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(mMainBinding.root)
		mMainBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
			when (item.itemId) {
				R.id.navigation_home -> {
					replaceScreen(0)
					mMainBinding.fab.setOnClickListener {
						supportFragmentManager.beginTransaction()
							.add(AddingPurchaseFragment(), "adding")
							.commit()
					}
				}
				R.id.navigation_dashboard -> {
					replaceScreen(1)
					mMainBinding.fab.setOnClickListener {
						supportFragmentManager.beginTransaction()
							.add(AddingPlanningFragment(), "adding")
							.commit()
					}
				}
			}
			true
		}
		replaceScreen(0)

		mMainBinding.fab.setOnClickListener {
			supportFragmentManager.beginTransaction()
				.add(AddingPurchaseFragment(), "adding")
				.commit()
		}

	}

	override fun onDestroy() {
		super.onDestroy()
		mMainPresenter.detachView()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.main_menu, menu)
		return true
	}

	@RequiresApi(Build.VERSION_CODES.M)
	override fun onOptionsItemSelected(item: MenuItem): Boolean =
		when (item.itemId) {
			R.id.save_csv -> {
				if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
					val root = getExternalFilesDir(null)?.absolutePath
					if (!root.isNullOrEmpty())
						mMainPresenter.saveAll(root)
					else showMsg("error")
				} else {
					ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_STORAGE)
				}
				true
			}
			R.id.logout -> {
				mMainPresenter.logOut()
				true
			}
			R.id.add_pin -> {
				val intent = Intent(applicationContext, PincodeActivity::class.java)
				intent.putExtra("login", false)
				startActivity(intent)
				true
			}
			R.id.cancel_pin -> {
				PinPreferences(this).removePin() // TODO move to presenter
				true
			}
			R.id.clear_recycler -> {
				mMainPresenter.clear()
				true
			}
			else -> true
		}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		if (requestCode == WRITE_STORAGE &&
			grantResults.isNotEmpty() &&
			grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			val root = getExternalFilesDir(null)?.absolutePath
			if (!root.isNullOrEmpty())
				mMainPresenter.saveAll(getExternalFilesDir(null)!!.absolutePath)
			else showMsg("error")
		}
	}

	private fun replaceScreen(position: Int) {
		var pos = position
		if (pos < 0 || pos >= mScreens.size) {
			pos = 0
		}
		supportFragmentManager
			.beginTransaction()
			.replace(mMainBinding.container.id, mScreens[pos])
			.commit()
	}

	override fun showMsg(string: String) {
		Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
		Log.d(TAG, string)
	}

	override fun toAuthActivity() {
		startActivity(Intent(applicationContext, AuthActivity::class.java))
		finish()
	}

	companion object {
		const val TAG = "MAIN"
		const val WRITE_STORAGE = 1
	}
}
