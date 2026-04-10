plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.room3)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.shinoaki.shoppingmall"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.shinoaki.shoppingmall"
        minSdk = 24
        targetSdk = 36
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
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.viewpager2)
    implementation(libs.datastore.preference)
    implementation(libs.swiperefreshlayout)
//    刷新组件
    implementation(libs.smartRefreshLayout.refresh.layout.kernel)
    implementation(libs.smartRefreshLayout.refresh.header.classics)
    implementation(libs.smartRefreshLayout.refresh.header.radar)
    implementation(libs.smartRefreshLayout.refresh.header.falsify)
    implementation(libs.smartRefreshLayout.refresh.header.material)
    implementation(libs.smartRefreshLayout.refresh.header.two.level)
    implementation(libs.smartRefreshLayout.refresh.footer.ball)
    implementation(libs.smartRefreshLayout.refresh.footer.classics)
    implementation(libs.glide)
    implementation(libs.androidx.room3.runtime)
    //使用 ksp
//    implementation(libs.androidx.room3.compiler)
    ksp(libs.androidx.room3.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}