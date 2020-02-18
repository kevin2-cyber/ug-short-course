buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0-alpha09")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("com.bugsnag:bugsnag-android-gradle-plugin:4.7.3")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://sdk.squareup.com/public/android") }
    }
}

tasks {
    register("delete", Delete::class) {
        delete(buildDir)
    }
}
