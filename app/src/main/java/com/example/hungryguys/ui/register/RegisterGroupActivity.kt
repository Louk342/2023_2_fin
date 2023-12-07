package com.example.hungryguys.ui.register

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import com.example.hungryguys.databinding.ActivityRegisterGroupBinding
import com.example.hungryguys.utills.ActivityUtills
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

enum class GroupItem {
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

        doLocation()

        // TODO: DB에 그룹 저장하는 것 추가해야함
        binding.registerButton.setOnClickListener {
            when (currentPlace) {
                "" -> Toast.makeText(applicationContext, "그룹을 선택해주세요", Toast.LENGTH_SHORT).show()
                "현재 위치" ->Toast.makeText(applicationContext, "설정할 수 없는 그룹입니다.", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(applicationContext, "그룹 설정 완료", Toast.LENGTH_SHORT).show()
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

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf(
            GroupItem.group_name.name to "동양미래대학교",
            GroupItem.group_lat.name to "37.500049",
            GroupItem.group_lng.name to "126.868003",
        )
        val data2 = mutableMapOf(
            GroupItem.group_name.name to "구로성심병원",
            GroupItem.group_lat.name to "37.499632",
            GroupItem.group_lng.name to "126.866363",
        )
        val data3 = mutableMapOf(
            GroupItem.group_name.name to "고척스카이돔",
            GroupItem.group_lat.name to "37.498230",
            GroupItem.group_lng.name to "126.867020",
        )
        dbdata.add(data1)
        dbdata.add(data2)
        dbdata.add(data3)

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
            binding.groupTitle.text = it.title.toString()
            currentPlace = it.title.toString()
            true
        }

        fLC = LocationServices.getFusedLocationProviderClient(this)
        callback = object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                var list = p0.locations
                var location = list[0]
                var latLng = LatLng(location.latitude, location.longitude)
                val position = CameraPosition.Builder().target(latLng).zoom(16.0f).build()
                googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

                val options = MarkerOptions()
                options.position(latLng)
                options.title("현재 위치")
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                val marker = googleMap?.addMarker(options)
                marker?.showInfoWindow()
            }
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        fLC?.requestLocationUpdates(locationRequest, callback!!, Looper.getMainLooper())
    }
}