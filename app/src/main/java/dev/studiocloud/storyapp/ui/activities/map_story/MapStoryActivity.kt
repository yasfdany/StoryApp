package dev.studiocloud.storyapp.ui.activities.map_story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.data.ResultData
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.databinding.ActivityMapStoryBinding
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class MapStoryActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapStoryBinding
    private lateinit var mMap: GoogleMap
    private lateinit var viewModelFactory: ViewModelFactory
    private var storyViewModel: StoryViewModel? = null

    private fun obtainStoryViewModel(): StoryViewModel {
        viewModelFactory = ViewModelFactory.getInstance(this)
        return ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = obtainStoryViewModel()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        with(binding){
            ibBack.setOnClickListener { finish() }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style)
        )

        storyViewModel?.getStoryLocation()?.observe(this){ result ->
            when(result){
                is ResultData.Error -> {}
                is ResultData.Loading -> {}
                is ResultData.Success -> {
                    for (story: StoryItem in result.data ?: arrayListOf()){
                        val position = LatLng(
                            story.lat ?: 0.0,
                            story.lon ?: 0.0
                        )
                        mMap.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title(story.name)
                                .snippet(story.description)
                        )
                    }

                    val startingPosition = LatLng(
                        result.data?.get(0)?.lat ?: 0.0,
                        result.data?.get(0)?.lon ?: 0.0
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPosition, 8f))
                }
            }
        }
    }
}