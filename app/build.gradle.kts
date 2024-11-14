plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.zdy.wallpaperinstallapp"
    compileSdk = 35



    defaultConfig {
        applicationId = "com.zdy.wallpaperinstallapp"
        minSdk = 21
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        viewBinding = true
    }


}

dependencies {

    val daggerHiltVersion = "2.51.1"
    val coroutineLifecycleScopesVersion = "2.8.6"
    val roomVersion = "2.6.1"

    // ----- Hilt ------ //
    implementation (group= "javax.inject", name= "javax.inject", version= "1")

    implementation("com.google.dagger:hilt-android:${daggerHiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${daggerHiltVersion}")
    //-------------------//

    // --- Glide --- //
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")
    //----------------//

    // --- Coroutines --- //
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //------------------//


    // --- Coroutine Lifecycle Scopes --- //
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:${coroutineLifecycleScopesVersion}}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${coroutineLifecycleScopesVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${coroutineLifecycleScopesVersion}")
    //------------------------------------//


    // --- Room --- //
    implementation("androidx.room:room-ktx:${roomVersion}")
    annotationProcessor("androidx.room:room-compiler:${roomVersion}")
    kapt("androidx.room:room-compiler:${roomVersion}")
    //--------------//

    // --- Retrofit --- //
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //------------------//

    // --- Bottom sheet --- //
    implementation("com.google.android.material:material:1.12.0")
    //----------------------//

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}