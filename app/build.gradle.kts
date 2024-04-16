import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    //hilt
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}

hilt {
    enableAggregatingTask = true
}

android {
    namespace = "com.example.githubapi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.githubapi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType(type = KaptGenerateStubsTask::class) {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material:material:1.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation ("com.android.support.test:rules:1.0.1")
    androidTestImplementation ("com.android.support.test:runner:1.0.1")

    //MockK
    testImplementation ("io.mockk:mockk:1.13.2")
    androidTestImplementation ("io.mockk:mockk-android:1.13.2")

    //compose destination
    implementation ("io.github.raamcosta.compose-destinations:core:1.7.27-beta")

    //hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // Okhttp
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.6")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.6")

    //coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //lifecycle-compose
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    //fragment ktx for ViewModel injection
    implementation ("androidx.fragment:fragment-ktx:1.5.4")

    //glide(landscapist)
    implementation ("com.github.skydoves:landscapist-glide:1.5.0")
}

kapt {
    correctErrorTypes = true
}