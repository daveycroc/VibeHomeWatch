package com.vibe.homeapp

import android.content.ComponentName
import androidx.wear.protolayout.material3.*
import androidx.wear.protolayout.material3.ButtonDefaults.iconButtonStyle
import androidx.wear.tiles.*
import kotlinx.coroutines.flow.first

class HomeControlTileService : Material3TileService() {

    override suspend fun MaterialScope.tileResponse(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile {
        
        // 1. Fetch the pinned devices from our DataStore
        val pinned = DeviceRepo.getPinnedDevices(this@HomeControlTileService).first()

        return tile(
            timeline = timeline(
                timelineEntry(
                    // 2. PrimaryLayout provides the M3 "Expressive" structure
                    primaryLayout(
                        titleSlot = { 
                            text("Home".layoutString, typography = Typography.LABEL_MEDIUM) 
                        },
                        mainSlot = {
                            // 3. MultiButtonLayout creates the grid for us
                            buttonGroup {
                                pinned.take(4).forEach { device ->
                                    buttonGroupItem {
                                        iconButton(
                                            onClick = clickable(
                                                id = device.id,
                                                // Sends click to our Receiver.kt
                                                action = action(ComponentName(
                                                    this@HomeControlTileService, 
                                                    HomeControlReceiver::class.java
                                                ))
                                            ),
                                            iconContent = {
                                                // Matches icon to device type
                                                icon(imageResource(getIconForType(device.type)))
                                            },
                                            style = iconButtonStyle(
                                                // Vibe: Blue for active, Surface for inactive
                                                colors = if (device.isActive) 
                                                    ButtonColors.m3Blue() else ButtonColors.m3Surface()
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )
                )
            )
        )
    }

    private fun getIconForType(type: String) = when(type) {
        "LIGHT" -> "ic_light"
        "FAN" -> "ic_fan"
        else -> "ic_home"
    }
}
