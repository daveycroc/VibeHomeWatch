VibeHomeWatch/ (Your Repo Root)
├── build.gradle.kts           <-- Project-wide settings
├── settings.gradle.kts        <-- Module names
├── app/
│   ├── build.gradle.kts       <-- App-specific dependencies
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml  <-- The "Brain" (Permissions/Tiles)
│           ├── proto/
│           │   └── devices.proto    <-- The DataStore Schema
│           └── java/com/vibe/homeapp/
│               ├── MainActivity.kt  <-- The Picker UI
│               ├── TileService.kt   <-- The Tile Logic
│               ├── Receiver.kt      <-- The Click Handler
│               └── Network.kt       <-- The Ktor Service
