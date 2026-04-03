# Installing to Nvidia Shield

This guide covers the necessary steps to build a release version of this Android TV app and install it directly onto an Nvidia Shield TV via ADB over the network.

## Prerequisites

1.  **Android SDK / ADB**: Ensure that the `adb` (Android Debug Bridge) command is available in your terminal path.
2.  **Network Access**: Your computer and the Nvidia Shield must be connected to the same local network.

## 1. Preparing the Nvidia Shield

To install apps via ADB, you must first enable Developer Options and Network Debugging on your Shield.

1.  Navigate to **Settings** (gear icon in the top right).
2.  Go to **Device Preferences** > **About**.
3.  Scroll down to **Build** and click on it **7 times** quickly. You should see a toast message saying "You are now a developer!".
4.  Go back to **Device Preferences** and scroll down to the newly visible **Developer options**.
5.  Turn on **Network debugging**. Wait a moment, and it will display an IP address and port (e.g., `192.168.1.50:5555`). Note down this IP address.

## 2. Building the Release APK

To create a signed release APK ready for installation, you'll need to use Gradle on your local machine.

Run the following command from the root of the repository:

```sh
JELLYFIN_VERSION=1.0.0 ./gradlew assembleRelease
```

This process will compile and build the release app. The resulting APK file will typically be generated in the output directory:
`app/build/outputs/apk/release/jellyfin-androidtv-v1.0.0-release.apk`


## 3. Connecting to the Shield via ADB

Open your terminal and connect to the Shield using the IP address you noted in step 1:

```sh
adb connect <SHIELD_IP_ADDRESS>
```
*Replace `<SHIELD_IP_ADDRESS>` with the actual address (e.g., `adb connect 192.168.1.50`).*

The **first time** you do this, a prompt will appear on your Nvidia Shield TV asking to "Allow network debugging".
Check **"Always allow from this computer"** and select **OK**.

Verify the connection:
```sh
adb devices
```
You should see your device IP listed with the status `device`.

## 4. Installing the App

Once connected, you can install the recently built APK using the following command:

```sh
adb install -r app/build/outputs/apk/release/jellyfin-androidtv-v1.0.0-release.apk
```

*Note: The `-r` flag allows you to overwrite/update an existing installation without losing the app's local data.*

If the installation succeeds, adb will output:
```
Success
```

## 5. Launch the App

You can now find the app in the Nvidia Shield's main app drawer.
Alternatively, you can launch it directly from your terminal using ADB:

```sh
adb shell monkey -p org.jellyfin.androidtv -c android.intent.category.LAUNCHER 1
```
*(Remember to update the package name `org.jellyfin.androidtv` if the `applicationId` was changed in your custom fork).*

## 6. Disconnecting (Optional)

When you are finished debugging or installing, it is good practice to disconnect:

```sh
adb disconnect <SHIELD_IP_ADDRESS>
```
