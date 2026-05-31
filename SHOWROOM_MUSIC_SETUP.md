# Showroom Music System Setup

## Overview
This is a customized version of SimpMusic configured as a **Showroom Music System** - a background audio player for commercial spaces like showrooms, retail stores, and offices.

## Features

### Audio-Only Playback
- ✅ **Video disabled by default** - Only plays audio tracks
- ✅ **No video popups** - Clean audio-only experience
- ✅ **Background playback** - Continues playing when app is minimized

### Built-in Features
- 🎵 Stream music from YouTube Music (audio only)
- 📻 AI Radio - Auto-play related songs
- ⏱️ Advertisement breaks at configurable intervals
- ⏰ Prayer alarm at scheduled times
- 📋 Queue management

## Building the App

### Prerequisites
- Android Studio or Gradle
- YouTube Music API access (configurable)

### Build Commands

```bash
# Navigate to project directory
cd Devijewelelrs-avdt

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

### APK Location
- Debug: `androidApp/build/outputs/apk/debug/`
- Release: `androidApp/build/outputs/apk/release/`

## Customization

### Changing App Name
Edit: `androidApp/src/main/res/values/app_name.xml`
```xml
<resources>
    <string name="app_name">Your Showroom Name</string>
</resources>
```

### Changing App Icon
1. Replace icons in:
   - `androidApp/src/main/res/mipmap-*/`
   - `androidApp/src/main/res/drawable/`

2. Update foreground: `ic_launcher_foreground.xml`
3. Update background: `ic_launcher_background.xml`

### Changing Branding Text
Edit: `composeApp/src/commonMain/composeResources/values/strings.xml`
```xml
<string name="showroom_welcome">Your Showroom Name</string>
<string name="audio_only_mode">Audio Only • Background Play</string>
```

### Advertisement Settings
Configure in app Settings > Advertisement & Prayer:
- Interval: 30 seconds to 5 minutes
- Upload custom advertisement audio files
- Set prayer alarm time and audio

## Desktop Icon (Linux/Windows)

### Linux
1. Copy the APK to your system
2. Use `android-tools` or `adb` to install
3. Create desktop entry in `~/.local/share/applications/`

### Windows
1. Transfer APK to Android device
2. Or use Android Studio's APK Analyzer

## Troubleshooting

### Music not playing?
- Check internet connection
- Verify YouTube Music authentication
- Check if device is in "Do Not Disturb" mode

### Advertisement not appearing?
- Ensure advertisement audio files are uploaded in Settings
- Check advertisement interval is set correctly

### App crashes on startup?
- Clear app data and reinstall
- Check for missing API configuration

## License
This customization is based on SimpMusic (MIT License)
Original project: https://github.com/maxrave-dev/SimpMusic
