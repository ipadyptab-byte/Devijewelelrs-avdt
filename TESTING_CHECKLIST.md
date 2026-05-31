# Showroom Music System - Testing Checklist

## 📱 Installation
- [ ] Build APK: `./gradlew assembleDebug`
- [ ] Install APK on Android device
- [ ] Grant permissions (Storage, Notifications, etc.)

---

## 🔇 Audio-Only Mode Tests

### Video Settings Hidden
- [ ] Open Settings
- [ ] Verify "Play video for video track" option is NOT visible
- [ ] Verify "Video Quality" option is NOT visible
- [ ] Verify "Video Download Quality" option is NOT visible

### Audio Playback Only
- [ ] Play any song from Home screen
- [ ] Verify NO video popup appears
- [ ] Verify audio plays correctly
- [ ] Minimize app - audio continues playing
- [ ] Play from Playlist - audio only
- [ ] Play from Search - audio only
- [ ] Play from AI Radio - audio only

---

## 🎵 Music Playback Tests

### Home Screen
- [ ] Browse home sections (Quick Picks, Playlists, etc.)
- [ ] Tap on song to play
- [ ] Verify Now Playing screen shows
- [ ] Play/Pause works
- [ ] Next/Previous works
- [ ] Shuffle works
- [ ] Repeat works (Off → All → One)

### Queue Management
- [ ] View queue (swipe up or tap queue icon)
- [ ] Add songs to queue
- [ ] Reorder songs in queue
- [ ] Remove song from queue
- [ ] Clear queue

### Background Playback
- [ ] Play song and minimize app
- [ ] Check notification - music controls visible
- [ ] Use notification controls (play/pause/next)
- [ ] Lock screen - controls still work

---

## 📻 AI Radio Tests

### Access AI Radio
- [ ] Tap queue icon (🎵) in header
- [ ] OR go to Settings → AI Radio
- [ ] AI Radio screen opens

### Search and Play
- [ ] Type song/artist name in search
- [ ] Results appear
- [ ] Tap song to play
- [ ] Related songs auto-add to queue
- [ ] Music plays continuously

### Radio Controls
- [ ] "Start Radio" button works
- [ ] "Stop Radio" button stops auto-play
- [ ] Skip to next related song
- [ ] Queue shows AI Radio tracks

---

## ⏱️ Advertisement Tests

### Settings Configuration
- [ ] Go to Settings → Advertisement & Prayer
- [ ] Find "Advertisement Playback" toggle
- [ ] Enable advertisement feature
- [ ] Select interval (30s, 1min, 2min, 5min)
- [ ] Upload advertisement audio file
- [ ] File uploads successfully
- [ ] Preview advertisement file

### Advertisement Playback
- [ ] Play any song
- [ ] Wait for advertisement interval
- [ ] Current song pauses
- [ ] Advertisement plays
- [ ] Advertisement ends
- [ ] Original song resumes
- [ ] Timer resets for next ad

### Edge Cases
- [ ] Disable advertisement - verify no ads play
- [ ] Change interval - verify new timing
- [ ] Change advertisement file - verify new file plays
- [ ] Long song + ad interval - ad plays at correct time

---

## ⏰ Prayer Alarm Tests

### Settings Configuration
- [ ] Go to Settings → Advertisement & Prayer
- [ ] Find "Prayer Alarm" section
- [ ] Enable prayer alarm
- [ ] Set alarm time (e.g., 11:30 AM)
- [ ] Upload prayer audio file
- [ ] File uploads successfully

### Alarm Trigger
- [ ] Set alarm for 1 minute ahead (for testing)
- [ ] Wait for alarm time
- [ ] If music playing - it pauses
- [ ] Prayer audio plays
- [ ] Prayer audio ends
- [ ] Music resumes automatically

### Alarm Edge Cases
- [ ] Disable alarm - verify no alarm triggers
- [ ] Change alarm time - verify new time
- [ ] Change prayer file - verify new file plays
- [ ] Alarm while no music playing - prayer still plays?

---

## 🎨 UI/Branding Tests

### App Appearance
- [ ] App name shows "Showroom Music" on home screen
- [ ] Header shows "Showroom Music"
- [ ] Subtitle shows "Audio Only Mode"
- [ ] App icon shows music note
- [ ] Blue theme visible in icon

### Settings Screen
- [ ] All settings accessible
- [ ] No video-related options visible
- [ ] Advertisement & Prayer section visible
- [ ] AI Radio section visible

---

## 🔧 General Functionality Tests

### Navigation
- [ ] Home tab works
- [ ] Library tab works
- [ ] Search tab works
- [ ] Settings tab works
- [ ] Back navigation works correctly

### Account (if logged in)
- [ ] YouTube Music login works
- [ ] Liked songs sync
- [ ] Playlists load correctly
- [ ] History tracked

### App Stability
- [ ] App doesn't crash on startup
- [ ] No crashes when playing music
- [ ] No crashes in Settings
- [ ] Memory usage acceptable

---

## 📊 Performance Tests

### Memory
- [ ] Monitor RAM usage while playing
- [ ] Check for memory leaks (long playback)

### Battery
- [ ] Background playback battery usage acceptable
- [ ] No excessive drain when minimized

### Network
- [ ] Works on WiFi
- [ ] Works on mobile data (if applicable)
- [ ] Handles offline gracefully

---

## ✅ Test Summary

| Feature | Status | Notes |
|---------|--------|-------|
| Audio-Only Mode | ? | |
| Home Playback | ? | |
| AI Radio | ? | |
| Queue Management | ? | |
| Background Playback | ? | |
| Advertisement | ? | |
| Prayer Alarm | ? | |
| Branding | ? | |
| Settings | ? | |

---

## 🐛 Bug Report Template

```
Device: [Model & Android Version]
Build: [Debug/Release]
Date: [Test Date]

**Issue:**
[Description]

**Steps to Reproduce:**
1. 
2. 
3. 

**Expected:**
[What should happen]

**Actual:**
[What happens]

**Screenshots/Logs:**
[Attach if available]
```

---

## 📞 Need Help?

If you find bugs or issues:
1. Note the exact steps to reproduce
2. Take screenshots
3. Check device logs (adb logcat)
4. Report with the template above
