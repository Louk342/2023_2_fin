package com.example.hungryguys.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import com.example.hungryguys.MainActivity
import com.example.hungryguys.databinding.ActivityRegisterGroupBinding
import com.example.hungryguys.ui.searchrestaurant.RestaurantItemId
import com.example.hungryguys.utills.ActivityUtills
import com.example.hungryguys.utills.Request
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray

enum class GroupItem {
    /** 그룹 아이디 */
    group_id,
    /** 그룹 이름 */
    group_name,
    /** 그룹 위도 */
    group_lat,
    /** 그룹 경도 */
    group_lng
}
class RegisterGroupActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityRegisterGroupBinding
    private var mMap: GoogleMap? = null
    var fLC : FusedLocationProviderClient? = null
    var callback : LocationCallback? = null
    var currentPlace: String = ""
    lateinit var currentPosition: LatLng
    lateinit var type: String
    val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 상테바, 하단 내비게이션 투명화 및 보정
        val activityUtills = ActivityUtills(this)
        activityUtills.setStatusBarTransparent()
        activityUtills.setStatusBarAllPadding(binding.root)

        // 툴바 설정
        val toolbar = binding.toolbar
        toolbar.setContentInsetsRelative(0, 0)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        type = intent.getStringExtra("type").toString()

        if (type == "change") {
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }

        doLocation()

        binding.registerButton.setOnClickListener {
            when (currentPlace) {
                "" -> Toast.makeText(applicationContext, "그룹을 선택해주세요", Toast.LENGTH_SHORT).show()
                "현재 위치" ->Toast.makeText(applicationContext, "설정할 수 없는 그룹입니다.", Toast.LENGTH_SHORT).show()
                else -> {
                    dbdata.forEach{
                        if (it[GroupItem.group_name.name] == binding.groupTitle.text.toString()) {
                            // TODO: DB에 그룹id 저장하는 것으로 수정해야함
                            Toast.makeText(applicationContext, "${it[GroupItem.group_id.name]} 설정 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if(type == "register") startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    fun doLocation() {
        val mapFragment = supportFragmentManager.findFragmentById(com.example.hungryguys.R.id.mapView) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    // NULL이 아닌 GoogleMap 객체를 파라미터로 제공해 줄 수 있을 때 호출
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        val loadGroupThread = Thread {
            val restaurantJson = Request.reqget("${Request.REQUSET_URL}/group") ?: JSONArray()
            //추후 그룹 아이디를 db에서

            for (i in 0..< restaurantJson.length()) {
                val json = restaurantJson.getJSONObject(i)

                val data= mutableMapOf(
                    GroupItem.group_id.name to json.getString("group_id"),
                    GroupItem.group_name.name to json.getString("group_name"),
                    GroupItem.group_lat.name to json.getString("x"),
                    GroupItem.group_lng.name to json.getString("y"),
                )
                dbdata.add(data)
            }
        }
        loadGroupThread.start()
        loadGroupThread.join()

        dbdata.forEach{
            val groupLatLng = LatLng(it[GroupItem.group_lat.name]!!.toDouble(), it[GroupItem.group_lng.name]!!.toDouble())

            val markerOptions = MarkerOptions().run {
                position(groupLatLng)
                title(it[GroupItem.group_name.name])
            }
            googleMap.addMarker(markerOptions)
        }

        googleMap.setOnMarkerClickListener{
            it.showInfoWindow()
            val position = CameraPosition.Builder().target(it.position).zoom(16.0f).build()
            googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))
            binding.groupTitle.text = it.title.toString()
            currentPlace = it.title.toString()
            true
        }

        fLC = LocationServices.getFusedLocationProviderClient(this)
        callback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                var list = p0.locations
                var location = list[0]
                var latLng = LatLng(location.latitude, location.longitude)
                currentPosition = latLng
                val position = CameraPosition.Builder().target(latLng).zoom(16.0f).build()
                googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

                val options = MarkerOptions()
                options.position(latLng)
                options.title("현재 위치")
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                val marker = googleMap?.addMarker(options)
                marker?.showInfoWindow()
                fLC?.removeLocationUpdates(callback!!)
            }
        }

        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        fLC?.requestLocationUpdates(locationRequest, callback!!, Looper.getMainLooper())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> { // 뒤로 가기 버튼 눌렀을 때
                finish() // 액티비티 종료
            }
        }
        return super.onOptionsItemSelected(item)
    }
}