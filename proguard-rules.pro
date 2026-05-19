# Add project specific ProGuard rules here.
-keep class com.grameenlight.app.model.** { *; }
-keep class com.grameenlight.app.data.local.entity.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
# Firebase
-keep class com.google.firebase.** { *; }
# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
