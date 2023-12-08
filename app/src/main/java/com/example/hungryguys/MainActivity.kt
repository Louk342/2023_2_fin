package com.example.hungryguys

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hungryguys.databinding.ActivityMainBinding
import com.example.hungryguys.databinding.AppBarMainBinding
import com.example.hungryguys.databinding.NavHeaderMainBinding
import com.example.hungryguys.utills.ActivityUtills
import com.example.hungryguys.utills.GoogleLoginData
import com.example.hungryguys.utills.Request
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var actionbarView: AppBarMainBinding
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    private lateinit var activityUtills: ActivityUtills
    lateinit var groupName: String
    lateinit var navHeaderBinding: NavHeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        actionbarView = binding.appBarMain
        setContentView(binding.root)

        // 상단 상테바, 하단 내비게이션 투명화 및 보정
        activityUtills = ActivityUtills(this)
        activityUtills.setStatusBarTransparent()
        activityUtills.setStatusBarAllPadding(actionbarView.root)

        setSupportActionBar(actionbarView.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navView = binding.navView
        drawerLayout = binding.drawerLayout

        // 모든 프래그먼트는 이 navController가 담당전환하고 싶은프래그먼트는 mobile_navigation에 등록하고
        // navController.navigate(mobile_navigation 등록해놓은 프래그먼트id) 로 프래그먼트 이동
        navController = findNavController(R.id.nav_host_fragment_content_main)

        // 내비게이션 해더부분 바인딩
        navHeaderBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))

        // 로그인한 이름으로
        navHeaderBinding.userName.text = GoogleLoginData.name

        // 프사 클릭하면 마이페이지로
        navHeaderBinding.imageView.setOnClickListener {
            navController.navigate(R.id.nav_mypage)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_searchparty, R.id.nav_searchrestaurant
            ), drawerLayout
        )

        navController.addOnDestinationChangedListener(navControllerEvent)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // 프래그먼트 변화시 발생하는 이벤트, 액션바 화면조정때문에 쓰임
    private val navControllerEvent =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            // 액션바 전체요소
            actionbarView.mainTitleLayout.visibility = View.GONE //메인화면 전용 레이아웃
            actionbarView.serchViewLayout.visibility = View.GONE //검색창
            actionbarView.actionBarTitle.visibility = View.GONE //액션바 타이틀
            actionbarView.settingButton.visibility = View.GONE // 설정 버튼 아이콘
            actionbarView.searchIconButton.visibility = View.GONE //오른쪽 검색아이콘

            // 여기에 활성화 하고싶은 요소만 View.VISIBLE 로
            when (destination.id) {
                // 홈 프래그먼트
                R.id.nav_home -> {
                    // 그룹 이름 변경
                    setGroupName(actionbarView.actionBarTitle).start()
                    actionbarView.actionBarTitle.visibility = View.VISIBLE
                    actionbarView.searchIconButton.visibility = View.VISIBLE
                    actionbarView.mainTitleLayout.visibility = View.VISIBLE
                }
                // 식당찾기 프래그먼트
                R.id.nav_searchrestaurant -> {
                    actionbarView.searchText.text = null
                    actionbarView.serchViewLayout.visibility = View.VISIBLE
                }
                // 파티찾기 프래그먼트
                R.id.nav_searchparty -> {
                    actionbarView.actionBarTitle.text = getString(R.string.title_searchparty)
                    actionbarView.actionBarTitle.visibility = View.VISIBLE
                }
                // 마이페이지 프래그먼트
                R.id.nav_mypage -> {
                    actionbarView.actionBarTitle.text = getString(R.string.menu_mypage)
                    actionbarView.settingButton.visibility = View.VISIBLE
                    actionbarView.actionBarTitle.visibility = View.VISIBLE
                }
                //설정페이지 프래그먼트
                R.id.nav_settings -> {
                    actionbarView.actionBarTitle.text = getString(R.string.menu_settings)
                    actionbarView.actionBarTitle.visibility = View.VISIBLE
                }
            }
        }

    // 유저 아이디로 그룹이름 가져오기
    fun setGroupName(textView: TextView): Thread  {
        return Thread {
            val userdataJson = Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}") ?: JSONArray()
            val group_id = userdataJson.getJSONObject(0).getString("group_id")

            val groupJson = Request.reqget("${Request.REQUSET_URL}/group") ?: JSONArray()

            for (i in 0..< groupJson.length()) {
                val json = groupJson.getJSONObject(i)

                if(json.getString("group_id") == group_id) {
                    groupName = json.getString("group_name")

                    runOnUiThread {
                        navHeaderBinding.userLocation.text = groupName
                        textView.text = groupName
                    }
                    return@Thread
                }
            }
        }
    }

    // 키보드 이외에 다른요소 선택시 키보드 닫치게
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        activityUtills.closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}