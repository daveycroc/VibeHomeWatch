package com.vibe.homeapp

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

// 1. Tell DataStore how to read/write the Device data
object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}

// 2. Create the DataStore instance (Top-level property)
val Context.deviceDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_prefs.pb",
    serializer = UserPreferencesSerializer
)

// 3. The Repository object for your UI and Tile to use
object DeviceRepo {
    
    // Save a device to the Tile's list
    suspend fun pinDevice(context: Context, device: Device) {
        context.deviceDataStore.updateData { currentPrefs ->
            currentPrefs.toBuilder()
                .addFavoriteDevices(device)
                .build()
        }
    }

    // Remove a device from the list
    suspend fun unpinDevice(context: Context, deviceId: String) {
        context.deviceDataStore.updateData { currentPrefs ->
            val index = currentPrefs.favoriteDevicesList.indexOfFirst { it.id == deviceId }
            if (index >= 0) {
                currentPrefs.toBuilder().removeFavoriteDevices(index).build()
            } else {
                currentPrefs
            }
        }
    }

    // Get the flow of devices for the Tile to display
    fun getPinnedDevices(context: Context): Flow<List<Device>> =
        context.deviceDataStore.data.map { it.favoriteDevicesList }
}
