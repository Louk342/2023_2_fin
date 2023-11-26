package com.example.hungryguys

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.hungryguys.databinding.ActivityMainBinding
import com.example.hungryguys.databinding.NavHeaderMainBinding
import com.example.hungryguys.utills.ActivityUtills

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 상테바, 하단 내비게이션 투명화
        val activityUtills = ActivityUtills(this)
        activityUtills.setStatusBarTransparent(binding.appBarMain.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayShowTitleEnabled(false)

        binding.appBarMain.fab.setOnClickListener { view ->
            //리뷰추가 엑티비티 생성 후 연동 필요
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        val drawerLayout = binding.drawerLayout
        val navView = binding.navView
        // 내비게이션 해더부분 바인딩
        val navHeaderBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_searchparty, R.id.nav_searchrestaurant
            ), drawerLayout
        )

        navController.addOnDestinationChangedListener(navControllerEvent)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // 네비게이션뷰 화면전환 이벤트
    private val navControllerEvent = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home, R.id.nav_searchrestaurant -> {
                    binding.appBarMain.serchViewLayout.visibility = View.VISIBLE
                    binding.appBarMain.actionBarTitle.visibility = View.GONE
                }
                R.id.nav_searchparty -> {
                    binding.appBarMain.actionBarTitle.text = getString(R.string.menu_searchparty)
                    binding.appBarMain.serchViewLayout.visibility = View.GONE
                    binding.appBarMain.actionBarTitle.visibility = View.VISIBLE
                }
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}