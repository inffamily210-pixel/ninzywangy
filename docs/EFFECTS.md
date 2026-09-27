# 300+ Effect Presets

Ninzywangy kini memiliki katalog **350 effect preset** yang dikelompokkan ke dalam 10 kategori: Color, Blur & Focus, Distortion, Light, Glitch, Particle, Stylize, Transition, Audio Reactive, dan Chroma.

File `EffectCatalog.kt` menyediakan metadata effect, ID stabil, parameter dasar, pencarian ID, dan filter kategori. Preset ini sudah dapat dipakai untuk membangun panel pemilih effect/keyframe.

## Catatan implementasi

Katalog dan UI browser sudah tersedia, tetapi setiap effect masih merupakan preset metadata. Agar menghasilkan gambar/video nyata, setiap ID perlu dihubungkan ke implementasi renderer OpenGL ES, Canvas, atau pipeline video. Jangan mengklaim effect sudah merender sebelum shader/renderer-nya diimplementasikan.
