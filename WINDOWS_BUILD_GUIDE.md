# Windows Desktop Application - Build Guide

This project (SimpMusic / Showroom Music) already has **built-in Windows desktop support** using Kotlin Multiplatform + Compose Desktop!

## Quick Build Instructions

### Prerequisites (Windows)

1. **Java 21+** required
   - Download from: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
   - Install and set JAVA_HOME

2. **Git** (optional, for cloning)
   - Download from: https://git-scm.com/download/win

3. **Gradle** (included in project)
   - Uses Gradle Wrapper (gradlew.bat)

### Build Commands

Open **PowerShell** or **Command Prompt** in the project folder:

```powershell
# Navigate to project
cd Devijewelelrs-avdt

# Build Windows executable (creates .exe)
./gradlew.bat :desktopApp:package

# Or run directly for testing
./gradlew.bat :desktopApp:run
```

### Build Tasks Available

| Task | Description |
|------|------------|
| `:desktopApp:run` | Run app directly (no install) |
| `:desktopApp:package` | Build Windows executable |
| `:desktopApp:packageRelease` | Build optimized release |
| `:desktopApp:buildWindowsAmd64` | Build for Windows x64 |

### Output Location

After building:
```
desktopApp/build/output/
├── compose/
│   └── desktopApp-*.exe    # Main executable
└── ...
```

## System Requirements

- **OS**: Windows 10 or later
- **RAM**: 4GB minimum (8GB recommended)
- **Disk**: 500MB for app + VLC natives
- **Java**: JDK 21+

## Features on Windows Desktop

✅ Full music playback (audio only)
✅ AI Radio - search & auto-play
✅ Advertisement intervals
✅ Prayer alarm
✅ Background playback
✅ System tray support
✅ Custom branding ("Showroom Music")
✅ Audio-only mode (no video)

## Customizing for Windows

### Change App Name
Edit `desktopApp/src/jvmMain/kotlin/com/maxrave/simpmusic/Main.kt` or app_name.xml

### Change Icon
Replace icons in `composeApp/icon/`:
- `circle_app_icon.ico` (Windows)
- `circle_app_icon.png` (Linux/Mac)

### Branding
All showroom branding changes in `composeApp` affect desktop too.

## Troubleshooting

### "JAVA_HOME not set"
```powershell
# Find Java path
where java

# Set JAVA_HOME
setx JAVA_HOME "C:\path\to\jdk-21"
# Then restart PowerShell
```

### "Gradle build failed"
```powershell
# Clean and rebuild
./gradlew.bat clean
./gradlew.bat :desktopApp:package --info
```

### "VLC not found"
The app bundles VLC natives. If missing:
```powershell
# Create directory
mkdir vlc-natives\windows

# Download VLC from https://www.videolan.org/
# Extract to vlc-natives\windows\
```

## Distribution

### Share .exe Directly
```powershell
# Find the built executable
Get-ChildItem -Path desktopApp -Filter "*.exe" -Recurse
```

### Create Installer (Advanced)
Use tools like:
- **Inno Setup**: Free Windows installer
- **NSIS**: Another option
- **Conveyor**: Included in project (see desktopApp/build.gradle.kts)

## Quick Start Script

Create `build-windows.bat`:
```batch
@echo off
echo Building Showroom Music for Windows...

cd /d "%~dp0"

echo Checking Java...
java -version

echo Building...
call gradlew.bat :desktopApp:package

echo.
echo Build complete!
echo Check: desktopApp\build\output\compose\
pause
```

## Support

If build fails:
1. Check Java version (`java -version` should show 21+)
2. Run with `--info` flag for detailed logs
3. Check Gradle wrapper is executable

---
**Note**: Windows 32-bit (x86) not supported. Use 64-bit Windows.
