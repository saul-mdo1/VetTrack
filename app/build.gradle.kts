plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    //FIREBASE
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.vettrack"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vettrack"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")
    implementation("com.google.firebase:firebase-messaging-ktx:24.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    implementation("com.google.firebase:firebase-messaging")

    //TIMBER
    implementation("com.jakewharton.timber:timber:5.0.1")

    //MATERIAL
    implementation("com.google.android.material:material:1.12.0")

    //KOIN
    implementation("io.insert-koin:koin-android:3.5.0")

    //GLIDE
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

    //NOTIFICATIONS
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.core:core-ktx:1.12.0")
}