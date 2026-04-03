# Custom Deviations Tracker

This file documents all custom modifications made to this fork diverging from the official `jellyfin-androidtv` upstream repository. AI agents must check this file before regenerating code to ensure custom logic is preserved.

## 1. DTS:X Audio Playback Support
- **Purpose**: Enhanced ExoPlayer to properly recognize and pass-through DTS:X audio formats.
- **Affected Files**:
  - `playback/core/src/main/kotlin/mediastream/MediaStream.kt`
  - `playback/jellyfin/src/main/kotlin/mediastream/tracks.kt`
  - `playback/media3/exoplayer/src/main/kotlin/support/mediaStreamToFormat.kt`
  - `gradle/libs.versions.toml`
- **Agent Instructions**: Ensure `androidx-media3` is >= 1.10.0 and `jellyfin-androidx-media` is >= 1.9.0+1. If upstream changes their audio pipeline, ensure the `DtsX` branches in data class mapping and ExoPlayer Format mapping remain intact.

## 2. Release Keystore Configuration
- **Purpose**: Automates signed release packaging via gradle using a local keystore.
- **Affected Files**:
  - `app/build.gradle.kts`
  - `jellyfin-release-key.jks` (Do not commit to public repo)
- **Agent Instructions**: Keep the `signingConfigs { release }` block in custom gradle updates.

## 3. Custom Name Replacement
- **Purpose**: Replaced the "Jellyfin" name with "JoHennifin" across UI visible strings.
- **Affected Files**:
  - `app/src/main/res/**/strings.xml`
  - `fastlane/metadata/**/*.txt`
  - `app/src/main/res/mipmap-*/*.png`
  - `fastlane/metadata/**/*.png`
- **Agent Instructions**: Ensure new UI components or imported upstream translations retain the custom "JoHennifin" name instead of "Jellyfin" strings, and ensure all logos follow the original color styling with the updated branding name.

## 4. Enhanced Dolby Vision and HDR10 Capability Detection
- **Purpose**: Refines Dolby Vision profile detection (separating Profile 5 and 8) and HDR10 reporting to ensure the server transcodes unsupported Dolby Vision content instead of failing during direct play.
- **Affected Files**:
  - `app/src/main/java/org/jellyfin/androidtv/util/profile/MediaCodecCapabilitiesTest.kt`
  - `app/src/main/java/org/jellyfin/androidtv/util/profile/deviceProfile.kt`
  - `app/src/main/java/org/jellyfin/androidtv/util/profile/deviceProfileReport.kt`
  - `playback/media3/exoplayer/src/main/kotlin/ExoPlayerBackend.kt`
- **Agent Instructions**: Maintain the distinct checks for Dolby Vision Profile 5 vs Profile 8 in `MediaCodecCapabilitiesTest` and ensure `deviceProfile.kt` uses these specific checks to populate the unsupported range types. Do not revert to the broad `hasCodecForMime` check for Dolby Vision support. Also, keep the additional error logging in `ExoPlayerBackend`.
