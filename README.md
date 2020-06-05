# Kobalt AppFilter
This is an Android app that is a simple wrapper of [Appfilter.net](https://appfilter.net/) website.

## Features
- Search Google Play Store database through provided filters
- Should be compatible from Android 2.3+ (older versions might work, too)
- Written mostly in Kotlin (minor exceptions for workarounds)
- Using minimal amount of libraries to keep compatibility with older Android versions
- No XML layouts, most of resources are written inside of code (except for raw)
- Separated application module from Android framework as much as possible for now
- Optimized APK should be around 650 KB, RAM usage around 15-20 MB