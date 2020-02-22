plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("com.bugsnag.android.gradle")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("kotlin-android-extensions")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file(rootProject.file("keystore/readium.jks"))
            storePassword = "shortcourse_readium"
            keyAlias = "readium"
            keyPassword = "shortcourse_readium"
        }
    }
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "shortcourse.readium"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "0.0.1"
        setProperty("archivesBaseName", "$applicationId-v$versionName($versionCode)")

        // Manifest placeholders
        addManifestPlaceholders(mapOf("bugsnag_key" to "1bd231a720cd45c44ed2ae2bc7ec1c4d"))

        // Set Build Configuration Fields
        buildConfigField(
            "String", "READIUM_VERSION", "\"$applicationId-v$versionName($versionCode)\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        named("debug") {
            buildConfigField(
                "String",
                "SQUARE_APP_ID",
                "\"sandbox-sq0idb-pgfYgj6x48Ees6YXgf5g9w\""
            )
        }
        named("release") {
            buildConfigField(
                "String",
                "SQUARE_APP_ID",
                "\"sq0idp-4CJoUUG1OQU3YYtYtVDKp\""
            )
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Enable data & view binding
    dataBinding.isEnabledForTests = true
    buildFeatures.dataBinding = true
    buildFeatures.viewBinding = true
    buildToolsVersion = "30.0.0 rc1"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")

    // Core Module
    api(project(":core"))

    // Bugsnag => Crash reporting
    implementation("com.bugsnag:bugsnag-android:4.22.3")

    // Square payment API
    implementation("com.squareup.sdk.in-app-payments:card-entry:1.3.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Testing
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
