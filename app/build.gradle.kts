plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("vkid.manifest.placeholders")
}

android {
    namespace = "ru.kontakt"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.kontakt"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Desugaring https://developer.android.com/studio/write/java8-support.html#library-desugaring
        // Required when setting minSdkVersion to 20 or lower
        multiDexEnabled = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

            kotlinOptions {
                freeCompilerArgs = listOf("-Xdebug")
            }
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // VK ID SDK
    implementation(libs.vkid)
    implementation(libs.onetap.xml)
    implementation(libs.vk.sdk.support)
    // VK SDK
    implementation(libs.android.sdk.core)
    implementation(libs.android.sdk.api)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Glide configuration
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}

