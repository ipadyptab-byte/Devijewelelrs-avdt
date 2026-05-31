package com.maxrave.simpmusic.demo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.maxrave.domain.data.entities.AdvertisementTrackEntity
import com.maxrave.domain.manager.DataStoreManager
import com.maxrave.domain.repository.AdvertisementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Demo class for testing Advertisement and Prayer Alarm features.
 * 
 * Usage:
 * ```
 * val demo = AdvertisementDemo(context, dataStoreManager, advertisementRepository)
 * 
 * // Setup test data
 * demo.setupTestData()
 * 
 * // Test advertisement playback
 * demo.testAdvertisementPlayback()
 * 
 * // Test prayer alarm (triggers in 1 minute)
 * demo.testPrayerAlarm()
 * ```
 */
class AdvertisementDemo(
    private val context: Context,
    private val dataStoreManager: DataStoreManager,
    private val advertisementRepository: AdvertisementRepository,
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    /**
     * Setup all test data for demo
     */
    fun setupTestData() {
        scope.launch {
            // 1. Enable advertisement feature
            dataStoreManager.setAdvertisementEnabled(true)
            dataStoreManager.setAdvertisementInterval(60) // 60 seconds for demo
            
            // 2. Enable prayer alarm
            dataStoreManager.setPrayerAlarmEnabled(true)
            dataStoreManager.setPrayerAlarmTime("11:30")
            // Note: File path should be set to actual audio file
            
            println("✅ Demo data setup complete!")
            println("   - Advertisement enabled with 60 second interval")
            println("   - Prayer alarm enabled for 11:30 AM")
        }
    }

    /**
     * Add a test advertisement track
     */
    fun addTestAdvertisementTrack(title: String, filePath: String) {
        scope.launch {
            val track = AdvertisementTrackEntity(
                title = title,
                filePath = filePath,
                duration = 30000L, // 30 seconds
                inLibrary = System.currentTimeMillis()
            )
            advertisementRepository.insertAdvertisementTrack(track)
            println("✅ Added advertisement track: $title")
        }
    }

    /**
     * Test advertisement playback (manual trigger)
     * Call this to manually trigger an advertisement
     */
    suspend fun testAdvertisementPlayback(): AdvertisementTrackEntity? {
        return advertisementRepository.getLastAdvertisementTrack()
    }

    /**
     * Get current advertisement settings
     */
    suspend fun getAdvertisementSettings(): AdvertisementSettings {
        return AdvertisementSettings(
            enabled = dataStoreManager.advertisementEnabled.first() == DataStoreManager.Values.TRUE,
            intervalSeconds = dataStoreManager.advertisementInterval.first()
        )
    }

    /**
     * Get current prayer alarm settings
     */
    suspend fun getPrayerAlarmSettings(): PrayerAlarmSettings {
        return PrayerAlarmSettings(
            enabled = dataStoreManager.prayerAlarmEnabled.first() == DataStoreManager.Values.TRUE,
            time = dataStoreManager.prayerAlarmTime.first(),
            filePath = dataStoreManager.prayerAlarmFilePath.first()
        )
    }

    /**
     * Test prayer alarm - schedules alarm for 1 minute from now
     */
    fun testPrayerAlarmNow() {
        scope.launch {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val filePath = dataStoreManager.prayerAlarmFilePath.first()
            
            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                action = "com.simpmusic.PRAYER_ALARM"
                putExtra("prayer_file_path", filePath)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // Schedule for 1 minute from now
            val calendar = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 1)
            }
            
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                println("✅ Prayer alarm scheduled for ${calendar.time}")
            } catch (e: SecurityException) {
                println("❌ Cannot schedule alarm: ${e.message}")
            }
        }
    }

    /**
     * Disable all features (cleanup)
     */
    fun disableAll() {
        scope.launch {
            dataStoreManager.setAdvertisementEnabled(false)
            dataStoreManager.setPrayerAlarmEnabled(false)
            println("✅ All features disabled")
        }
    }
}

data class AdvertisementSettings(
    val enabled: Boolean,
    val intervalSeconds: Int
)

data class PrayerAlarmSettings(
    val enabled: Boolean,
    val time: String,
    val filePath: String
)
