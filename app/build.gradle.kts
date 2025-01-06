plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.android.ridesharing"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.ridesharing"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        // Exclude specific files to avoid conflicts
        exclude("META-INF/DEPENDENCIES")

        // You can also use include if you want to make sure the file is included
        // include("META-INF/DEPENDENCIES")

        // If there are other conflicting files, you can exclude them too:
        // exclude("META-INF/NOTICE")
        // exclude("META-INF/LICENSE")
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.firebase:firebase-auth:22.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation (libs.google.auth.library.oauth2.http)
//    implementation (libs.group.x.com.squareup.okhttp3.name)

    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation ("com.google.android.gms:play-services-base:17.6.0") // or latest version






}