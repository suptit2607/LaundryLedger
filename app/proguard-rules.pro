# SQLCipher rules
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }
-dontwarn net.sqlcipher.**
-dontwarn org.sqlite.**

# Hilt rules
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**

# Compose rules
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Room rules
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
