package com.dicoding.storyapp.view.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.ApiStoryConfig
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository, val context: Context) : ViewModel() {

    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()

    private lateinit var apiService: ApiService

    init {
        viewModelScope.launch {
            val token = getTokenFromDataStore()
            apiService = ApiStoryConfig.getApiService(token)
        }
    }

    private suspend fun getTokenFromDataStore(): String {
        return repository.getSession().first().token
    }

    fun addManyMarker(gm : GoogleMap) {
        mMap = gm

        viewModelScope.launch {
            try {
                val storyResponse = apiService.getStoriesWithLocation()
                if (!storyResponse.error) {
                    val storyList = storyResponse.listStory

                    storyList.forEach { story ->
                        val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(story.name)
                                .snippet(story.description)
                        )
                        boundsBuilder.include(latLng)
                    }

                    val bounds: LatLngBounds = boundsBuilder.build()
                    val resources = context.resources
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                } else {
                    println("Error in the response or response contains an error.")
                }
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }
}