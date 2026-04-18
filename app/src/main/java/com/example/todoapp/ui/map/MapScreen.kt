package com.example.todoapp.ui.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoapp.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

/** Stały marker — lokalizacja PJATK w Warszawie. */
private val PJATK = LatLng(52.2616, 21.0203)

/**
 * Ekran z mapą Google Maps.
 * Pokazuje stały marker uczelni i opcjonalnie bieżącą lokalizację użytkownika.
 */
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> viewModel.onPermissionResult(granted) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(PJATK, 14f)
    }

    val userLatLng = uiState.userLocation?.let {
        LatLng(it.latitude, it.longitude)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Stały marker uczelni
            Marker(
                state = MarkerState(position = PJATK),
                title = stringResource(R.string.map_campus_marker)
            )
            // Marker bieżącej lokalizacji — widoczny tylko po udzieleniu zgody
            userLatLng?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = stringResource(R.string.map_me_marker)
                )
            }
        }

        // Przycisk "Pokaż mnie" w prawym dolnym rogu
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            if (!uiState.permissionGranted) {
                Text(
                    text = stringResource(R.string.map_permission_rationale),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.widthIn(max = 240.dp)
                )
            }
            FloatingActionButton(
                onClick = {
                    if (uiState.permissionGranted) {
                        viewModel.fetchLocation()
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            ) {
                if (uiState.isLoadingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.map_center_on_me),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}