# Xposed Hebrew Reverse for Pebble

A module for the [Xposed Framework](http://repo.xposed.info/module/de.robv.android.xposed.installer) that hooks the original Pebble Android app (com.getpebble.android).

The purpose of this module is to reverse Hebrew characters before they are sent to the Pebble, and therefore fix Hebrew RTL issues.

Before using this module you must install a custom firmware on your Pebble that contains the Hebrew font.
You can do that by using the [PebbleBit](http://spebblebits.com/fonts/) service.

This module should be installed on an Android device that already has Xposed Installer installed.

To compile this module - get XposedBridgeApi.jar and put it on the root directory of this project (alongside the AndroidManifest).

# Download Link

You can find the most recently compiled APK at:
https://dl.dropboxusercontent.com/u/3819031/Hebrew%20Reverse%20for%20Pebble.apk
