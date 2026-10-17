plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.pongsakorn.saithonggroceryapp"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res/layouts/home",
                "src/main/res/layouts/products",
                "src/main/res/layouts/stock",
                "src/main/res/layouts/history",
                "src/main/res/layouts/report",
                "src/main/res/layouts/common",
                "src/main/res",
            )
        }
    }

    defaultConfig {
        applicationId = "com.pongsakorn.saithonggroceryapp"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}