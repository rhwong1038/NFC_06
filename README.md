# NFC URL Emulator

## Overview
NFC URL Emulator is an Android app that emulates an NFC tag to broadcast a specific URL. When an NFC reader device is brought near the phone running this app, it receives the URL as if reading from a physical NFC tag.

## Features
- Emulates an NFC tag without physical hardware
- Broadcasts a predefined URL (https://google.com)
- Works with NFC readers that can read NDEF formatted tags
- Simple user interface showing NFC status

## Requirements
- Android device with NFC and HCE capabilities (Android 4.4+)
- Minimum SDK: Android 10 (API 29)
- Target SDK: Android 13 (API 33)

## How to Use
1. Install the app on a compatible Android device
2. Enable NFC on your device
3. Launch the app
4. Bring an NFC reader device close to your phone
5. The reader should detect the URL

## Testing
Test the app using:
- Another Android device with an NFC reading app
- An iPhone with NFC capabilities (iPhone 7 or later)
- A dedicated NFC reader device

## Customization
To change the broadcasted URL:
1. Open `MyHostApduService.kt`
2. Modify the `URL` constant
3. Rebuild and reinstall the app

## Troubleshooting
If the NFC reader doesn't detect the emulated tag:
1. Ensure NFC is enabled on both devices
2. Make sure the app is running (can be in background)
3. Try adjusting the position of the devices
4. Restart the app and/or the NFC reader device

## Limitations
- The app must be running for NFC emulation to work
- Some NFC readers may have limitations in reading emulated tags
- The emulated tag is not persistent if the app is force-closed or the device restarts
