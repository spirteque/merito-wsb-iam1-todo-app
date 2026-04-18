package com.example.todoapp.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class MapUiState(
    val userLocation: Location? = null,
    val isLoadingLocation: Boolean = false,
    val permissionGranted: Boolean = false
)

/**
 * ViewModel dla MapScreen.
 * Pobiera bieżącą lokalizację użytkownika przez FusedLocationProviderClient.
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(permissionGranted = granted) }
        if (granted) fetchLocation()
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLocation = true) }
            try {
                val location = fusedLocationClient.lastLocation.await()
                _uiState.update { it.copy(userLocation = location, isLoadingLocation = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingLocation = false) }
            }
        }
    }
}