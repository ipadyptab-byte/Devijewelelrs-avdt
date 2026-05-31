# Advertisement & Prayer Alarm Demo Guide

This guide demonstrates how the new Advertisement and Prayer Alarm features work in SimpMusic.

## Feature Overview

### 1. Advertisement Playback
- Play locally stored advertisement tracks at configurable fixed intervals
- **Interval Range**: 30 seconds to 300 seconds (5 minutes)
- **Behavior**: Interrupts current playback, plays ad, then resumes

### 2. Prayer Alarm
- Daily reminder at a specified time (e.g., 11:30 AM)
- Triggers automatically at the same time every day
- **Behavior**: Interrupts current playback, plays prayer audio, then resumes

---

## Quick Start Demo

### Using the Demo Class

The easiest way to test the features is using the `AdvertisementDemo` class:

```kotlin
// Create demo instance
val demo = AdvertisementDemo(
    context = applicationContext,
    dataStoreManager = dataStoreManager,
    advertisementRepository = advertisementRepository
)

// Setup test data
demo.setupTestData()

// Add a test advertisement track
demo.addTestAdvertisementTrack(
    title = "Test Advertisement",
    filePath = "/path/to/your/advertisement.mp3"
)

// Get current settings
val adSettings = demo.getAdvertisementSettings()
val prayerSettings = demo.getPrayerAlarmSettings()

// Test prayer alarm (triggers in 1 minute)
demo.testPrayerAlarmNow()

// Disable all features when done
demo.disableAll()
```

---

## Demo Setup

### Step 1: Prepare Audio Files

First, prepare two audio files:

1. **Advertisement Track**: Any short audio file (e.g., `advertisement.mp3`)
2. **Prayer Audio**: Your prayer audio file (e.g., `prayer.mp3`)

Place them in a location accessible by the app (e.g., Downloads folder or app storage).

### Step 2: Configure Settings (via DataStore)

Since the UI hasn't been implemented yet, you can configure the settings programmatically:

```kotlin
// In your ViewModel or Activity:
// Enable advertisement feature
dataStoreManager.setAdvertisementEnabled(true)
dataStoreManager.setAdvertisementInterval(60) // Play ad every 60 seconds

// Enable prayer alarm
dataStoreManager.setPrayerAlarmEnabled(true)
dataStoreManager.setPrayerAlarmTime("11:30") // Set to 11:30 AM
dataStoreManager.setPrayerAlarmFilePath("/path/to/prayer.mp3")
```

### Step 3: Add Advertisement Track to Database

```kotlin
val advertisementTrack = AdvertisementTrackEntity(
    title = "My Advertisement",
    filePath = "/path/to/advertisement.mp3",
    duration = 30000L, // 30 seconds in milliseconds
    inLibrary = System.currentTimeMillis()
)
advertisementRepository.insertAdvertisementTrack(advertisementTrack)
```

---

## How It Works

### Advertisement Flow

```
┌─────────────────────────────────────────────────────────┐
│                    Main Playback                        │
│  ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌───────┐│
│  │ Track 1 │───▶│ Track 2 │───▶│ Track 3 │───▶│ Track ││
│  └─────────┘    └─────────┘    └─────────┘    └───────┘│
└─────────────────────────────────────────────────────────┘
       │              │              │              │
       │  30-300 sec  │              │              │
       ▼              ▼              ▼              ▼
┌─────────────────────────────────────────────────────────┐
│              ADVERTISEMENT PLAYBACK                     │
│  ┌─────────────────┐    ┌─────────────────────────────┐│
│  │ Advt plays here │───▶│ Resume main playback        ││
│  └─────────────────┘    └─────────────────────────────┘│
└─────────────────────────────────────────────────────────┘
```

### Prayer Alarm Flow

```
┌─────────────────────────────────────────────────────────┐
│                Prayer Alarm Schedule                   │
│                                                         │
│  11:30 AM ──────────▶ TRIGGERS DAILY                   │
│                                                         │
│  When triggered:                                       │
│  1. Pause current music                                 │
│  2. Play prayer audio                                  │
│  3. Resume music from where it stopped                 │
└─────────────────────────────────────────────────────────┘
```

---

## Code Integration Points

### 1. Initialize AdvertisementManager

In your `SharedViewModel` or a dedicated manager class:

```kotlin
class MyManager(
    context: Context,
    private val dataStoreManager: DataStoreManager,
    private val advertisementRepository: AdvertisementRepository,
) {
    private val advertisementManager = AdvertisementManager(
        context = context,
        dataStoreManager = dataStoreManager,
        advertisementRepository = advertisementRepository,
        coroutineScope = viewModelScope,
        onPlayAdvertisement = { filePath ->
            // Play the advertisement file
            player.playFile(filePath)
        },
        onPausePlayback = {
            // Pause current playback
            player.pause()
        },
        onResumePlayback = {
            // Resume from saved position
            player.seekTo(savedPosition)
            player.play()
        }
    )
    
    fun start() {
        advertisementManager.startAdvertisementMonitoring()
    }
}
```

### 2. Handle New Track

```kotlin
fun onNewTrackStarted() {
    advertisementManager.onNewSongStarted()
}
```

### 3. Handle Advertisement Finished

```kotlin
fun onAdvertisementFinished() {
    // Player has finished playing the ad
    viewModelScope.launch {
        advertisementManager.onAdvertisementFinished()
    }
}
```

### 4. Handle Prayer Alarm Trigger

The alarm is automatically scheduled when enabled:

```kotlin
// In onCreate or when settings change:
fun setupPrayerAlarm() {
    viewModelScope.launch {
        val filePath = dataStoreManager.prayerAlarmFilePath.first()
        if (filePath.isNotEmpty()) {
            advertisementManager.setupPrayerAlarm(filePath)
        }
    }
}
```

---

## Testing the Features

### Test Advertisement (Manual)

```kotlin
// Manually trigger an advertisement
fun testAdvertisement() {
    viewModelScope.launch {
        val track = advertisementRepository.getLastAdvertisementTrack()
        track?.let {
            // Pause current music
            player.pause()
            // Play advertisement
            player.playFile(it.filePath)
        }
    }
}
```

### Test Prayer Alarm

```kotlin
// Schedule alarm for testing (1 minute from now)
fun setupTestAlarm() {
    val calendar = Calendar.getInstance().apply {
        add(Calendar.MINUTE, 1)
    }
    
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, PrayerAlarmReceiver::class.java).apply {
        action = "com.simpmusic.PRAYER_ALARM"
        putExtra("prayer_file_path", "/path/to/prayer.mp3")
    }
    
    val pendingIntent = PendingIntent.getBroadcast(
        this, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}
```

---

## Database Schema

The `advertisement_track` table:

| Column    | Type     | Description                     |
|-----------|----------|----------------------------------|
| id         | INTEGER  | Primary key, auto-increment      |
| title     | TEXT     | Display title for the ad          |
| filePath  | TEXT     | Full path to the audio file      |
| duration  | INTEGER  | Duration in milliseconds         |
| inLibrary | INTEGER  | Timestamp when added             |

---

## Settings Stored in DataStore

| Key                      | Type    | Default | Description                    |
|--------------------------|---------|---------|--------------------------------|
| advertisement_enabled    | String  | FALSE   | Enable/disable feature         |
| advertisement_interval   | Int     | 300     | Interval in seconds (30-300)   |
| prayer_alarm_enabled    | String  | FALSE   | Enable/disable feature         |
| prayer_alarm_time        | String  | 11:30   | Time in HH:mm format           |
| prayer_alarm_file_path  | String  | ""      | Path to prayer audio file      |

---

## Next Steps (For Full Implementation)

1. **Add UI components** to `SettingScreen.kt` for:
   - Toggle switches for enabling features
   - Slider for advertisement interval (30-300 sec)
   - Time picker for prayer alarm
   - File picker for uploading tracks

2. **Integrate with existing player** in `MediaServiceHandlerImpl.kt`

3. **Add AndroidManifest entries** for:
   - `RECEIVE_BOOT_COMPLETED` permission (for prayer alarm)
   - `SCHEDULE_EXACT_ALARM` permission (Android 12+)
   - `PrayerAlarmReceiver` broadcast receiver

4. **Add translations** for new strings in:
   - `composeApp/src/commonMain/composeResources/`

---

## Example Test Code

```kotlin
// Test in MainActivity or ViewModel
class TestDemo {
    
    suspend fun setupTestData() {
        // 1. Enable advertisement
        dataStoreManager.setAdvertisementEnabled(true)
        dataStoreManager.setAdvertisementInterval(30) // 30 seconds
        
        // 2. Add test advertisement track
        val testAd = AdvertisementTrackEntity(
            title = "Test Ad",
            filePath = "/sdcard/Music/test-ad.mp3",
            duration = 10000L,
            inLibrary = System.currentTimeMillis()
        )
        advertisementRepository.insertAdvertisementTrack(testAd)
        
        // 3. Enable prayer alarm
        dataStoreManager.setPrayerAlarmEnabled(true)
        dataStoreManager.setPrayerAlarmTime("11:30")
        dataStoreManager.setPrayerAlarmFilePath("/sdcard/Music/prayer.mp3")
    }
}
```

This demo provides the foundation for the advertisement and prayer alarm features. The next step would be integrating the UI components in `SettingScreen.kt`. (Hướng dẫn demo này trình bày cách các tính năng quảng cáo và báo thức cầu nguyện hoạt động. Bước tiếp theo là tích hợp các thành phần giao diện người dùng.)
