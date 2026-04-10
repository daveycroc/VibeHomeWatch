package com.vibe.homeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.wear.compose.foundation.lazy.*
import androidx.wear.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import kotlinx.coroutines.launch
import androidx.wear.tiles.TileService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // This is the entry point for the Watch UI
            DevicePickerScreen()
        }
    }
}

@Composable
fun DevicePickerScreen() {
    // 1. Setup local state and scope
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    
    // 2. Observe the DataStore (The list of currently pinned devices)
    val pinnedDevices by DeviceRepo.getPinnedDevices(context).collectAsState(initial = emptyList())

    // Hardcoded list for demo - in a real app, you'd fetch this from your hub
    val availableDevices = listOf(
        Device.newBuilder().setId("light_1").setName("Living Room Light").build(),
        Device.newBuilder().setId("fan_1").setName("Kitchen Fan").build(),
        Device.newBuilder().setId("thermostat_1").setName("Thermostat").build(),
        Device.newBuilder().setId("lock_1").setName("Front Door Lock").build()
    )

    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ) {
            item {
                Text(
                    text = "Add to Tile",
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.padding(bottom = 8.dp, top = 20.dp)
                )
            }

            items(availableDevices) { device ->
                // Check if this specific device is already in our DataStore list
                val isPinned = pinnedDevices.any { it.id == device.id }

                ToggleChip(
                    checked = isPinned,
                    onCheckedChanged = { checked ->
                        scope.launch {
                            if (checked) {
                                DeviceRepo.pinDevice(context, device)
                            } else {
                                DeviceRepo.unpinDevice(context, device.id)
                            }
                            
                            // Signal the Tile to refresh its layout immediately
                            TileService.getUpdater(context)
                                .requestUpdate(HomeControlTileService::class.java)
                            
                            // Vibe check: Add a little vibration on selection
                            VibeEngine.playTick(context)
                        }
                    },
                    label = { Text(device.name) },
                    appIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    toggleControl = {
                        Icon(
                            imageVector = ToggleChipDefaults.switchIcon(checked = isPinned),
                            contentDescription = if (isPinned) "Unpin" else "Pin"
                        )
                    },
