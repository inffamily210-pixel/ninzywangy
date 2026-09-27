# Ninzywangy Android Starter

Project Android Studio + UI mockup editor video berbasis Jetpack Compose.

## Yang sudah tersedia
- Canvas preview editor dalam orientasi landscape.
- Tool rail: media, text, shape, effect, audio.
- Timeline multi-layer dengan scrubber frame.
- Tombol play/pause dan pemilihan layer.
- Inspector transform dan mockup keyframe.
- Tombol export sebagai titik integrasi pipeline render.

## Menjalankan
1. Buka folder ini di Android Studio Hedgehog atau lebih baru.
2. Tunggu Gradle sync selesai.
3. Jalankan pada emulator/device Android API 24+.
4. APK debug dapat dibuat dengan `./gradlew assembleDebug`.

Ini adalah starter/MVP UI; engine render video, penyimpanan project, dan encoder export perlu diintegrasikan pada tahap berikutnya.
