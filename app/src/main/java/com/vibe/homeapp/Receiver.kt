class HomeControlReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val deviceId = intent.getStringExtra("device_id") ?: return
        
        // Use a GlobalScope or a dedicated Service scope for fire-and-forget
        CoroutineScope(Dispatchers.IO).launch {
            val success = HomeNetworkService.toggleDevice(deviceId)
            
            if (success) {
                // Perform a "Success" haptic click
                VibrationManager.confirm(context)
            } else {
                // Perform a "Failure" double-buzz
                VibrationManager.error(context)
            }
            
            // Tell the Tile to refresh its UI (e.g., change icon color)
            TileService.getUpdater(context)
                .requestUpdate(HomeControlTileService::class.java)
        }
    }
}

object VibeEngine {
    
    fun playTick(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
        vibrator.defaultVibrator.vibrate(effect)
    }

    fun playSuccess(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        // A "heavy click" followed by a light decay
        val effect = VibrationEffect.createWaveform(
            longArrayOf(0, 10, 40, 20), // Timing: Off, On, Off, On
            intArrayOf(0, 255, 0, 100), // Amplitudes
            -1
        )
        vibrator.defaultVibrator.vibrate(effect)
    }

    fun playError(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        // A sharp double-buzz
        val effect = VibrationEffect.createWaveform(
            longArrayOf(0, 50, 50, 50),
            intArrayOf(0, 255, 0, 255),
            -1
        )
        vibrator.defaultVibrator.vibrate(effect)
    }
}
