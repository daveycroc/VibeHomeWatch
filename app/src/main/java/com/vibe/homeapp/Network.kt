object HomeNetworkService {
    private val client = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = 2000 // 2 seconds is the "vibe" limit
        }
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun toggleDevice(deviceId: String): Boolean {
        return try {
            // Replace with your actual Hub URL (Home Assistant, Hubitat, etc.)
            val response = client.post("https://your-home-hub.local/api/services/light/toggle") {
                header("Authorization", "Bearer ${BuildConfig.HUB_TOKEN}")
                setBody(mapOf("entity_id" to deviceId))
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            false // Handle offline state
        }
    }
}
