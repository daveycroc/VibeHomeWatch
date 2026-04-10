// 1. Define the Tile Service
class HomeControlTileService : Material3TileService() {

    override suspend fun MaterialScope.tileResponse(
        requestParams: TileRequest
    ): Tile {
        // Here we'd fetch your "Chosen Devices" from local storage/DataStore
        val favoriteDevices = listOf("Lights", "Fan", "AC") 

        return tile(
            timeline = timeline(
                timelineEntry(
                    primaryLayout(
                        titleSlot = { text("Home Control".layoutString) },
                        mainSlot = {
                            // A grid of interactive buttons
                            row {
                                favoriteDevices.forEach { device ->
                                    iconButton(
                                        onClick = clickable(
                                            id = "toggle_$device",
                                            // This sends an intent back to our service to trigger the action
                                            action = action(ComponentName(context, HomeControlReceiver::class.java))
                                        ),
                                        iconContent = { 
                                            icon(imageResource("ic_${device.lowercase()}")) 
                                        }
                                    )
                                }
                            }
                        }
                    )
                )
            )
        )
    }
}
