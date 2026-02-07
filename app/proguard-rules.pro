# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Preserve line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Hide the original source file name for security
-renamesourcefileattribute SourceFile

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class dev.randheer094.dev.location.domain.**$$serializer { *; }
-keepclassmembers class dev.randheer094.dev.location.domain.** {
    *** Companion;
}
-keepclasseswithmembers class dev.randheer094.dev.location.domain.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# DataStore
-keep class androidx.datastore.*.** { *; }

# Koin
-keepclassmembers class * {
    public <init>(...);
}
-keepattributes Signature

# Keep mock location models
-keep class dev.randheer094.dev.location.domain.MockLocation { *; }