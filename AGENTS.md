# Agent Guidelines

Welcome to the custom fork of the jellyfin-androidtv repository. This document outlines the general information and guidelines that AI agents should follow when assisting with or modifying this project.

## Overview

- **Fork Status**: This repository is a custom fork of the official Jellyfin Android TV codebase.
- **Branch Strategy**: Changes and customizations are maintained on the most recent official release branch.
- **Upgrading**: When a new official release drops, follow the guide in [`NEW_RELEASE.md`](NEW_RELEASE.md) to pull in the official upstream changes.
- **Customizations**: All deviations from the official build must be documented in [`CUSTOM_DEVIATIONS.md`](CUSTOM_DEVIATIONS.md). Whenever you make a custom change to the build, ensure that it is tracked in that file.

## Technical Architecture

Based on the repository structure, here are the key technical findings to consider when modifying the project:
- **Build System**: The project uses Gradle with Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`).
- **Language**: The codebase is primarily written in Kotlin.
- **Application Structure**:
  - `:app`: Contains the main Application and UI layer.
  - `:playback:core`: Contains the core abstraction over media playback.
  - `:playback:jellyfin`: Jellyfin-specific media logic.
  - `:playback:media3:exoplayer` and `:playback:media3:session`: Integration with AndroidX Media3 and ExoPlayer for video and audio playback.
  - `:preference`: Manages application settings and preferences.
- **Dependencies**: The project heavily relies on the official `org.jellyfin.sdk`, integrating SDK packages for backend communication.


## User Devices

The user has the following devices which the app needs to work on:

- Nvidia Shield TV Pro (2019)
- Sony Bravia A80J (2021)

The app runs only on the Nvidia Shield. The Sony Bravia A80J is connected to the shield as the TV screen.