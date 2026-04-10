@Composable
fun DevicePickerScreen() {
    val devices = listOf("Living Room Light", "Kitchen Fan", "Thermostat", "Front Door Lock")
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() }, // Standard Wear OS clock at the top
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
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(devices) { device ->
                // A Chip is the standard Material 3 button for Wear OS
                ToggleChip(
                    checked = true, // This would be tied to your "Pinned" state
                    onCheckedChanged = { /* Logic to add/remove from Tile */ },
                    label = { Text(device) },
                    appIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    toggleControl = {
                        Icon(
                            imageVector = ToggleChipDefaults.switchIcon(checked = true),
                            contentDescription = "Pin to Tile"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
