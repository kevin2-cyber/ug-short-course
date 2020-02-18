plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("com.bugsnag.android.gradle")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "shortcourse.readium"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "0.0.1"

        addManifestPlaceholders(mapOf("bugsnag_key" to "1bd231a720cd45c44ed2ae2bc7ec1c4d"))
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")

    // Android
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.2.1")

    // Constraint Layout
    implementation("com.android.support.constraint:constraint-layout:2.0.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.1")
    implementation("androidx.navigation:navigation-runtime-ktx:2.2.1")

    // Material Components
    implementation("com.google.android.material:material:1.2.0-alpha04")

    // Bugsnag => Crash reporting
    implementation("com.bugsnag:bugsnag-android:4.22.3")

    // Square payment API
    implementation("com.squareup.sdk.in-app-payments:card-entry:1.3.0")

    // Lottie
    implementation("com.airbnb.android:lottie:3.3.1")

    // Anko
    implementation("org.jetbrains.anko:anko:0.10.8")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    // Firebase
    implementation("com.google.firebase:firebase-analytics:17.2.2")
    implementation("com.google.firebase:firebase-auth:19.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:21.4.0")
    implementation("com.google.firebase:firebase-storage:19.1.1")

    // Room
    implementation("androidx.room:room-runtime:2.2.3")
    annotationProcessor("androidx.room:room-compiler:2.2.3")
    implementation("androidx.room:room-ktx:2.2.3")

    // Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")

    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.3.1")

    // Lifecycle
    annotationProcessor("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.2.0")

    // Koin => Dependency Injection
    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-androidx-scope:2.0.1")
    implementation("org.koin:koin-androidx-viewmodel:2.0.1")

    // PhotoView
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // Store
    implementation("com.dropbox.mobile.store:store4:4.0.0-alpha03")

    // Testing
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
