plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.lucasbueno.thenamegamewt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lucasbueno.thenamegamewt"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Or for Kotlin Serialization
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    implementation ("com.squareup.okhttp3:okhttp:4.11.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    implementation("com.github.bumptech.glide:annotations:4.14.2")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

    implementation ("com.squareup.picasso:picasso:2.8")

    implementation ("io.coil-kt:coil:2.2.2")

    // Kotlin Serialization
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}